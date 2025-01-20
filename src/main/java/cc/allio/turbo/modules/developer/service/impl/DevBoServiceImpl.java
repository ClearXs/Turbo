package cc.allio.turbo.modules.developer.service.impl;

import cc.allio.turbo.common.event.Subscription;
import cc.allio.turbo.common.db.mybatis.service.impl.TurboCacheCrudServiceImpl;
import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.common.util.VariationAnalyzer;
import cc.allio.turbo.modules.developer.constant.AttributeType;
import cc.allio.turbo.modules.developer.constant.DatasetSource;
import cc.allio.turbo.modules.developer.domain.BoAttrSchema;
import cc.allio.turbo.modules.developer.domain.BoAttributeTree;
import cc.allio.turbo.modules.developer.domain.BoSchema;
import cc.allio.turbo.modules.developer.domain.TableColumns;
import cc.allio.turbo.modules.developer.entity.DevBo;
import cc.allio.turbo.modules.developer.entity.DevBoAttribute;
import cc.allio.turbo.modules.developer.entity.DevDataset;
import cc.allio.turbo.modules.developer.mapper.DevBoMapper;
import cc.allio.turbo.modules.developer.service.IDevBoAttributeService;
import cc.allio.turbo.modules.developer.service.IDevBoService;
import cc.allio.turbo.modules.developer.service.IDevDataSourceService;
import cc.allio.turbo.modules.developer.service.IDevDatasetService;
import cc.allio.uno.core.datastructure.tree.TreeSupport;
import cc.allio.uno.core.function.lambda.MethodBiFunction;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.core.util.comparator.Comparators;
import cc.allio.uno.core.util.id.IdGenerator;
import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.tx.TransactionContext;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class DevBoServiceImpl extends TurboCacheCrudServiceImpl<DevBoMapper, DevBo> implements IDevBoService {

    private final IDevDatasetService datasetService;
    private final IDevBoAttributeService boAttributeService;
    private final IDevDataSourceService dataSourceService;

    @Override
    @Transactional
    public boolean save(DevBo entity) {
        if (entity.getId() == null) {
            entity.setId(IdGenerator.defaultGenerator().getNextId());
        }
        // 生成dataset
        DevDataset dataset = new DevDataset();
        dataset.setName(entity.getName());
        dataset.setCode(entity.getCode());
        dataset.setSource(DatasetSource.BO);
        dataset.setSourceId(entity.getId());
        // 1.保存数据
        // 2.创建新数据集并保存
        // 3.保存至缓存
        return super.save(entity)
                && datasetService.save(dataset)
                && cacheToSchema(entity.getId()) != null;
    }

    @Override
    public Boolean check(Long boId) {
        return getOptById(boId).isPresent();
    }

    @Override
    public Boolean materialize(Long boId) throws BizException {
        BoSchema schema = cacheToSchema(boId);
        List<BoAttributeTree> attrTree = BoAttrSchema.to(schema.getAttrs());
        Map<Long, TableColumns> idMaterializeTables = Maps.newHashMap();
        // 遍历属性树，构建当前bo的物化视图
        attrTree.stream()
                // table优先级大于field
                .sorted(Comparator.comparing(BoAttributeTree::getAttrType))
                .forEach(tree ->
                        tree.accept(element -> {
                            AttributeType attrType = element.getAttrType();
                            if (AttributeType.TABLE == attrType) {
                                Table table = Table.of(element.getField()).setComment(element.getName());
                                idMaterializeTables.put(element.getId(), new TableColumns(table));
                            } else {
                                TableColumns tableColumns = idMaterializeTables.get(element.getParentId());
                                if (tableColumns != null) {
                                    ColumnDef columnDef = element.toColumnDef();
                                    if (columnDef != null) {
                                        tableColumns.addColumn(columnDef);
                                    }
                                }
                            }
                        }));
        if (CollectionUtils.isEmpty(idMaterializeTables)) {
            return false;
        }
        // 可进行物化的表
        Collection<TableColumns> materializableTables = idMaterializeTables.values();
        tableColumnsMaterialize(schema.getDataSourceId(), materializableTables);
        return update(Wrappers.<DevBo>lambdaUpdate().set(DevBo::isMaterialize, true).eq(DevBo::getId, boId));
    }

    @Override
    public BoSchema cacheToSchema(String boKey) throws BizException {
        DevBo bo = getOne(Wrappers.<DevBo>lambdaQuery().eq(DevBo::getCode, boKey));
        if (bo == null) {
            return null;
        }
        return cacheToSchema(bo.getId());
    }

    /**
     * 基于对{@link TableColumns}进行物化处理
     * <p>该方法基于{@link VariationAnalyzer}进行差异分析，根据结果</p>
     * <ol>
     *     <li>新增，包含表名称的变化</li>
     *     <li><del>删除：</del>该操作放在{@link #remove(Wrapper)}中</li>
     *     <li>变化：在基于{@link VariationAnalyzer}进行{@link ColumnDef}进行差异分析</li>
     * </ol>
     *
     * @param dataSourceId         dataSourceId
     * @param materializableTables 可物化表集合
     * @return true if materialized success
     */
    private boolean tableColumnsMaterialize(Long dataSourceId, Collection<TableColumns> materializableTables) {
        Table[] tables = materializableTables.stream().map(TableColumns::getTable).toArray(Table[]::new);
        List<TableColumns> databaseTables = dataSourceService.showTables(dataSourceId, tables);
        // 差异分析，只比较新增、修改。删除在#remove中进行
        VariationAnalyzer<TableColumns, DSLName> tableAnalyzer =
                new VariationAnalyzer<>(
                        materializableTables,
                        databaseTables,
                        TableColumns::getTableName,
                        (o1, o2) -> {
                            // 比较表名
                            boolean nameChanged = !o1.getTableName().equals(o2.getTableName());
                            if (nameChanged) {
                                return true;
                            }
                            // 表名一致，比较每一个列
                            MethodBiFunction<ColumnDef, Collection<ColumnDef>, ColumnDef> finder =
                                    (bench, target) ->
                                            target.stream()
                                                    .filter(t -> bench.getDslName().equals(t.getDslName()))
                                                    .findFirst()
                                                    .orElse(null);
                            int compare = Comparators.collections(finder).compare(o1.getColumnDefs(), o2.getColumnDefs());
                            return compare < 0;
                        });
        VariationAnalyzer.AnalyzeResultSet<TableColumns, DSLName> resultSet = tableAnalyzer.analyze();
        return TransactionContext.anyMatchOpen()
                .then(() -> {
                    List<VariationAnalyzer.Result<TableColumns, DSLName>> tableAddition = resultSet.getAddition();
                    // 新增的表结构
                    if (CollectionUtils.isNotEmpty(tableAddition)) {
                        TableColumns[] addiableTableColumns = tableAddition.stream().map(VariationAnalyzer.Result::getSource).toArray(TableColumns[]::new);
                        return dataSourceService.createTables(dataSourceId, addiableTableColumns);
                    }
                    return false;
                })
                .then(() -> {
                    // 修改的表结构
                    List<VariationAnalyzer.Result<TableColumns, DSLName>> tableMutative = resultSet.getMutative();
                    return tableMutative.stream()
                            .allMatch(result -> {
                                TableColumns source = result.getSource();
                                TableColumns bench = result.getBench();
                                // 如果表名称存在变化，则会放在新增处，故该地方判断column变化结果集，根据差异分析作出变化的差集
                                VariationAnalyzer<ColumnDef, DSLName> columnAnalyzer =
                                        new VariationAnalyzer<>(
                                                source.getColumnDefs(),
                                                bench.getColumnDefs(),
                                                ColumnDef::getDslName,
                                                // 1.比较名称
                                                // 2.比较类型
                                                // 3.比较precision scale
                                                (o1, o2) -> !o1.equalsTo(o2));
                                VariationAnalyzer.AnalyzeResultSet<ColumnDef, DSLName> columnResultSet = columnAnalyzer.analyze();
                                if (columnResultSet.changed()) {
                                    return dataSourceService.alertTable(
                                            dataSourceId,
                                            f -> {
                                                // 新增的
                                                List<VariationAnalyzer.Result<ColumnDef, DSLName>> addition = columnResultSet.getAddition();
                                                ColumnDef[] addiableColumns = addition.stream().map(VariationAnalyzer.Result::getSource).toArray(ColumnDef[]::new);
                                                if (addiableColumns.length > 0) {
                                                    f.addColumns(addiableColumns);
                                                }
                                                // 改变的
                                                List<VariationAnalyzer.Result<ColumnDef, DSLName>> mutative = columnResultSet.getMutative();
                                                ColumnDef[] alternativeColumns = mutative.stream().map(VariationAnalyzer.Result::getSource).toArray(ColumnDef[]::new);
                                                if (alternativeColumns.length > 0) {
                                                    f.alertColumns(alternativeColumns);
                                                }
                                                // 减少的
                                                List<VariationAnalyzer.Result<ColumnDef, DSLName>> reduction = columnResultSet.getReduction();
                                                DSLName[] reductiveColumns = reduction.stream().map(VariationAnalyzer.Result::getBench).map(ColumnDef::getDslName).toArray(DSLName[]::new);
                                                if (reductiveColumns.length > 0) {
                                                    f.deleteColumns(reductiveColumns);
                                                }
                                                return f.from(source.getTable());
                                            }
                                    );
                                }
                                return false;
                            });
                })
                .commit();
    }

    @Override
    public BoSchema cacheToSchema(Long boId) {
        return getCache().get(boId, BoSchema.class, () -> this.toBoSchema(boId));
    }

    @Override
    public Boolean saveBoSchema(BoSchema boSchema) {
        String id = boSchema.getId();
        if (StringUtils.isBlank(id)) {
            return false;
        }
        getCache().put(id, boSchema);
        return true;
    }

    @Override
    public void doOnSubscribe() {
        boAttributeService.subscribeOn("saveOrUpdate").observe(this::onPushToSchema);
        boAttributeService.subscribeOn(boAttributeService::save).observe(this::onPushToSchema);
        boAttributeService.subscribeOn("saveBatch").observe(this::onPushToSchema);
        boAttributeService.subscribeOn(boAttributeService::updateById).observe(this::onPushToSchema);

        subscribeOn("removeByIds").observe(this::onRemove);
        subscribeOn(IDevBoService::materialize)
                .observe(
                        subscription ->
                                subscription.getParameter("boId", Long.class)
                                        .ifPresent(boId -> {
                                            BoSchema boSchema = toBoSchema(boId);
                                            if (boSchema != null) {
                                                saveBoSchema(boSchema);
                                            }
                                        }));
    }

    /**
     * 当{@link DevBoAttribute}变化时的操作
     *
     * @param subscription subscription
     */
    public void onPushToSchema(Subscription<DevBoAttribute> subscription) {
        // save or updateById or saveOrUpdate
        subscription.getDomain()
                .ifPresent(boAttribute -> {
                    Long boId = boAttribute.getBoId();
                    BoSchema boSchema = toBoSchema(boId);
                    if (boSchema != null) {
                        saveBoSchema(boSchema);
                    }
                });
        // saveBatch
        subscription.getParameter("entityList")
                .ifPresent(list -> {
                    if (list instanceof List<?> attributes && !attributes.isEmpty()) {
                        Object obj = attributes.get(0);
                        if (obj instanceof DevBoAttribute attr) {
                            BoSchema boSchema = toBoSchema(attr.getBoId());
                            if (boSchema != null) {
                                saveBoSchema(boSchema);
                            }
                        }
                    }
                });
    }

    /**
     * 当bo数据被删除时的操作
     * <p>该操作将会在同一个事物进行</p>
     * <ul>
     *     <li>移除数据表信息</li>
     *     <li>移除缓存信息</li>
     *     <li>移除{@link DevDataset}信息</li>
     *     <li>移除{@link DevBoAttribute}信息</li>
     * </ul>
     *
     * @param subscription subscription
     */
    private void onRemove(Subscription<DevBo> subscription) {
        List<?> ids = subscription.getParameter("list").map(List.class::cast).orElse(Collections.emptyList());
        TransactionContext.open()
                // 根据缓存信息移除创建表信息
                .then(() -> {
                    for (Object id : ids) {
                        if (id instanceof Long boId) {
                            BoSchema schema = cacheToSchema(boId);
                            List<String> tableNames = Lists.newArrayList();
                            List<BoAttributeTree> attrTree = BoAttrSchema.to(schema.getAttrs());
                            attrTree.forEach(tree ->
                                    tree.accept(element -> {
                                        if (AttributeType.TABLE == element.getAttrType()) {
                                            tableNames.add(element.getField());
                                        }
                                    }));
                            dataSourceService.dropTables(schema.getDataSourceId(), tableNames.toArray(String[]::new));
                        }
                    }
                })
                // 移除BoSchema的缓存
                .then(() -> getCache().remove(ids))
                // 删除由bo创建导致的dataset同步创建
                .then(() -> datasetService.remove(Wrappers.<DevDataset>lambdaQuery().in(DevDataset::getSourceId, ids)))
                // 删除由bo创建导致的boAttribute同步创建
                .then(() -> boAttributeService.remove(Wrappers.<DevBoAttribute>lambdaQuery().in(DevBoAttribute::getBoId, ids)))
                .commit();
    }

    /**
     * 基于boid转换为{@link BoSchema}对象，重写在查询数据库
     *
     * @param boId boId
     * @return BoSchema
     */
    private BoSchema toBoSchema(Long boId) {
        DevBo bo = getById(boId);
        if (bo == null) {
            return null;
        }
        List<DevBoAttribute> tree = boAttributeService.tree(Wrappers.<DevBoAttribute>lambdaQuery().eq(DevBoAttribute::getBoId, boId));
        List<BoAttributeTree> treeify = tree.stream().map(boAttributeService::entityToDomain).toList();
        return BoSchema.from(bo, TreeSupport.adjust(treeify));
    }
}
