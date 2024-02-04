package cc.allio.turbo.modules.developer.service.impl;

import cc.allio.turbo.common.db.event.Subscription;
import cc.allio.turbo.common.db.mybatis.service.impl.TurboCacheCrudServiceImpl;
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
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.core.util.id.IdGenerator;
import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.type.DataType;
import cc.allio.uno.data.tx.TransactionContext;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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
    public Boolean materialize(Long boId) {
        BoSchema schema = cacheToSchema(boId);
        List<BoAttributeTree> attrTree = BoAttrSchema.to(schema.getAttrs());
        Map<Long, TableColumns> idMaterializeTables = Maps.newHashMap();
        // 遍历属性树，构建当前bo的物化视图
        attrTree.stream()
                .sorted(Comparator.comparing(BoAttributeTree::getAttrType))
                .forEach(tree ->
                        tree.accept(element -> {
                            AttributeType attrType = element.getAttrType();
                            if (AttributeType.TABLE == attrType) {
                                Table table = Table.of(element.getField()).setComment(element.getName());
                                idMaterializeTables.put(element.getId(), new TableColumns(table));
                            } else {
                                TableColumns tableColumns = idMaterializeTables.get(element.getParentId());
                                // 赋了一个默认的precision域scale
                                DataType dataType = DataType.create(element.getType().getDslType());
                                if (element.getPrecision() != null) {
                                    dataType.setPrecision(element.getPrecision());
                                }
                                if (element.getScale() != null) {
                                    dataType.setScale(element.getScale());
                                }
                                ColumnDef columnDef = ColumnDef.builder()
                                        .dslName(DSLName.of(element.getField()))
                                        .isFk(element.isFk())
                                        .isPk(element.isPk())
                                        .isUnique(element.isUnique())
                                        .isNonNull(element.isNonNull())
                                        .dataType(dataType)
                                        .build();
                                tableColumns.addColumn(columnDef);
                            }
                        }));
        // 可进行物化的表
        Collection<TableColumns> materializableTables = idMaterializeTables.values();
        // 基于物化表查询数据库中存在的表
        List<TableColumns> databaseTables = dataSourceService.showTables(schema.getDataSourceId(), materializableTables.stream().map(TableColumns::getTable).toArray(Table[]::new));
        // 差异分析，只比较新增、修改，删除在#remove中进行
        VariationAnalyzer<TableColumns, DSLName> analyzer =
                new VariationAnalyzer<>(
                        materializableTables,
                        databaseTables,
                        TableColumns::getTableName,
                        (o1, o2) -> !o1.getTableName().equals(o2.getTableName()));
        VariationAnalyzer.AnalyzeResultSet<TableColumns, DSLName> resultSet = analyzer.analyze();
        List<VariationAnalyzer.Result<TableColumns, DSLName>> addition = resultSet.getAddition();
        if (CollectionUtils.isNotEmpty(addition)) {
            dataSourceService.createTables(schema.getDataSourceId(), addition.stream().map(VariationAnalyzer.Result::getSource).toArray(TableColumns[]::new));
        }
        return update(Wrappers.<DevBo>lambdaUpdate().set(DevBo::isMaterialize, true).eq(DevBo::getId, boId));
    }

    @Override
    public BoSchema cacheToSchema(Long boId) {
        return getCache()
                .get(boId, BoSchema.class, () -> this.toBoSchema(boId));
    }

    @Override
    public boolean saveBoSchema(BoSchema boSchema) {
        String id = boSchema.getId();
        if (StringUtils.isBlank(id)) {
            return false;
        }
        getCache().put(id, boSchema);
        return true;
    }

    @Override
    public String getCacheName() {
        return "bo";
    }

    @Override
    public void doOnSubscribe() {
        Consumer<Subscription<DevBoAttribute>> postToSchema =
                subscription -> {
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
                };
        boAttributeService.subscribeOn("saveOrUpdate").observe(postToSchema);
        boAttributeService.subscribeOn(boAttributeService::save).observe(postToSchema);
        boAttributeService.subscribeOn("saveBatch").observe(postToSchema);
        boAttributeService.subscribeOn(boAttributeService::updateById).observe(postToSchema);
        boAttributeService.subscribeOn("removeByIds").observe(postToSchema);

        // 移除缓存信息
        getProxy().subscribeOn("removeByIds")
                .observe(subscription ->
                        subscription.getParameter("list").ifPresent(list -> {
                            if (list instanceof List<?> ids) {
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
                                                    dataSourceService.dropTables(schema.getDataSourceId(), tableNames.toArray(new String[0]));
                                                }
                                            }
                                        })
                                        // 移除BoSchema的缓存
                                        .then(() -> getCache().remove((Collection<Object>) ids))
                                        // 删除由bo创建导致的dataset同步创建
                                        .then(() -> datasetService.remove(Wrappers.<DevDataset>lambdaQuery().in(DevDataset::getSourceId, ((List<?>) list).toArray())))
                                        // 删除由bo创建导致的boAttribute同步创建
                                        .then(() -> boAttributeService.remove(Wrappers.<DevBoAttribute>lambdaQuery().in(DevBoAttribute::getBoId, ((List<?>) list).toArray())))
                                        .commit();
                            }
                        }));
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
