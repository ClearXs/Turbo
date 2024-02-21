package cc.allio.turbo.modules.developer.controller;

import cc.allio.turbo.common.constant.Direction;
import cc.allio.turbo.common.db.mybatis.helper.Conditions;
import cc.allio.turbo.common.domain.TreeDomain;
import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.common.web.R;
import cc.allio.turbo.common.web.params.EntityTerm;
import cc.allio.turbo.common.web.params.QueryParam;
import cc.allio.turbo.modules.developer.constant.FieldType;
import cc.allio.turbo.modules.developer.domain.BoAttrSchema;
import cc.allio.turbo.modules.developer.domain.BoSchema;
import cc.allio.turbo.modules.developer.entity.DomainEntity;
import cc.allio.turbo.modules.developer.service.IBoDomainService;
import cc.allio.uno.data.orm.dsl.type.TypeRegistry;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.util.Optionals;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dev/bo/domain")
@AllArgsConstructor
@Tag(name = "领域对象")
public class BoDomainController {

    private final IBoDomainService domainService;

    @Operation(summary = "保存")
    @PostMapping("/save/{boId}")
    public R<Boolean> save(@PathVariable("boId") Long boId, @RequestBody DomainEntity domain) throws BizException {
        boolean saved = domainService.save(boId, domain);
        return R.ok(saved);
    }

    @Operation(summary = "修改")
    @PutMapping("/edit/{boId}")
    public R<Boolean> edit(@PathVariable("boId") Long boId, @RequestBody DomainEntity domain) throws BizException {
        boolean updated = domainService.updateById(boId, domain);
        return R.ok(updated);
    }

    @Operation(summary = "保存或修改")
    @PostMapping("/save-or-update/{boId}")
    public R<Boolean> saveOrUpdate(@PathVariable("boId") Long boId, @RequestBody DomainEntity domain) throws BizException {
        boolean saveOrUpdate = domainService.saveOrUpdate(boId, domain);
        return R.ok(saveOrUpdate);
    }

    @Operation(summary = "批量保存或更新")
    @PostMapping("/batch-save-or-update/{boId}")
    public R<Boolean> batchSaveOrUpdate(@PathVariable("boId") Long boId, @RequestBody List<DomainEntity> domain) throws BizException {
        boolean batch = domainService.saveOrUpdateBatch(boId, domain);
        return R.ok(batch);
    }

    @Operation(summary = "删除")
    @DeleteMapping("/delete/{boId}")
    public R<Boolean> delete(@PathVariable("boId") Long boId, @RequestBody List<Long> ids) throws BizException {
        boolean removed = domainService.removeBatchByIds(boId, ids);
        return R.ok(removed);
    }

    @Operation(summary = "详情")
    @GetMapping("/details/{boId}/{id}")
    public R<DomainEntity> details(@PathVariable("boId") Long boId, @PathVariable("id") Long id) throws BizException {
        DomainEntity entity = domainService.getById(boId, id);
        return R.ok(entity);
    }

    @Operation(summary = "列表")
    @PostMapping("/list/{boId}")
    public R<List<DomainEntity>> list(@PathVariable("boId") Long boId, @RequestBody QueryParam<DomainEntity> params) throws BizException {
        QueryWrapper<DomainEntity> wrapper = Wrappers.query();
        domainService.getBoRepositoryOrThrow(boId)
                .inspectOn("list", context ->
                        Optionals.withBoth(context.getTypeFirst(QueryWrapper.class), context.getTypeFirst(BoSchema.class))
                                .ifPresent(pair -> {
                                    QueryWrapper<DomainEntity> queryWrapper = pair.getFirst();
                                    BoSchema boSchema = pair.getSecond();
                                    DomainConditions.domainQuery(boSchema, queryWrapper, params);
                                })
                );
        List<DomainEntity> entities = domainService.list(boId, wrapper);
        return R.ok(entities);
    }

    @Operation(summary = "分页")
    @PostMapping("/page/{boId}")
    public R<IPage<DomainEntity>> page(@PathVariable("boId") Long boId, @RequestBody QueryParam<DomainEntity> params) throws BizException {
        QueryWrapper<DomainEntity> wrapper = Wrappers.query();
        domainService.getBoRepositoryOrThrow(boId)
                .inspectOn("page", context ->
                        Optionals.withBoth(context.getTypeFirst(QueryWrapper.class), context.getTypeFirst(BoSchema.class))
                                .ifPresent(pair -> {
                                    QueryWrapper<DomainEntity> queryWrapper = pair.getFirst();
                                    BoSchema boSchema = pair.getSecond();
                                    DomainConditions.domainQuery(boSchema, queryWrapper, params);
                                })
                );
        IPage<DomainEntity> page = domainService.page(boId, params.getPage(), wrapper);
        return R.ok(page);
    }

    @Operation(summary = "导出")
    @PostMapping("/export/{boId}")
    public void export(@PathVariable("boId") Long boId, @RequestBody QueryParam<DomainEntity> params, HttpServletResponse response) {

    }

    @Operation(summary = "导入")
    @PostMapping("/import/{boId}")
    public R<Boolean> importFile(@PathVariable("boId") Long boId, @RequestBody MultipartFile file) {
        return null;
    }

    @Operation(summary = "树查询")
    @PostMapping("/tree/{boId}")
    public <Z extends TreeDomain<DomainEntity, Z>> R<List<Z>> tree(@PathVariable("boId") Long boId, @RequestBody QueryParam<DomainEntity> params) throws BizException {
        return null;
    }


    static class DomainConditions extends Conditions {

        public static <T extends DomainEntity> QueryWrapper<T> domainQuery(BoSchema boSchema, QueryParam<T> queryParam) {
            return domainQuery(boSchema, Wrappers.query(), queryParam);
        }

        public static <T extends DomainEntity> QueryWrapper<T> domainQuery(BoSchema boSchema, QueryWrapper<T> queryWrapper, QueryParam<T> queryParam) {
            BoAttrSchema boAttrSchema = boSchema.primarySchema();
            if (boAttrSchema == null) {
                return queryWrapper;
            }
            List<BoAttrSchema> attrSchemas = boAttrSchema.obtainFieldSchema();
            Map<String, BoAttrSchema> keyBoSchemas = attrSchemas.stream().collect(Collectors.toMap(BoAttrSchema::getKey, attr -> attr));
            // TODO 暂查询主表
            // query condition
            onQueryTerm(
                    queryParam,
                    keyBoSchemas::containsKey,
                    term -> {
                        BoAttrSchema field = keyBoSchemas.get(term.getField());
                        FieldType fieldType = field.getType();
                        Optional.ofNullable(fieldType.getDslType())
                                .map(dslType -> TypeRegistry.getInstance().findJavaType(dslType.getJdbcType()))
                                .map(javaType -> new EntityTerm(term, DomainEntity.class, field.getField(), javaType.getJavaType()))
                                .ifPresent(entityTerm -> {
                                    TermCondition termCondition = getCondByFieldType(entityTerm.getColumnType());
                                    termCondition.doCondition(entityTerm, queryWrapper);
                                });
                    });
            // order condition
            onOrderTerm(
                    queryParam,
                    keyBoSchemas::containsKey,
                    order -> queryWrapper.orderBy(true, Direction.ASC == order.getDirection(), order.getProperty()));

            return queryWrapper;
        }
    }
}
