package cc.allio.turbo.common.web;

import cc.allio.turbo.common.excel.util.ExcelUtil;
import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.common.db.entity.Entity;
import cc.allio.turbo.common.db.mybatis.help.Conditions;
import cc.allio.turbo.common.db.mybatis.service.ITurboCrudService;
import cc.allio.turbo.common.web.params.QueryParam;
import cc.allio.uno.core.util.ReflectTool;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 定义curd接口
 *
 * @param <T> 实体类型
 * @param <D> 领域类型
 * @param <S> 实体对应service类型
 * @author j.x
 * @date 2023/11/16 18:10
 * @since 0.1.0
 */
@Getter
public abstract class TurboCrudController<T extends Entity, D extends Entity, S extends ITurboCrudService<T>> extends TurboController {

    @Autowired
    protected S service;

    /**
     * 保存
     */
    @PostMapping("/save")
    @Operation(summary = "保存")
    public R<Boolean> save(@Validated @RequestBody D domain) {
        WebCrudInterceptor<T, D, S> interceptor = getInterceptor();
        T entity = interceptor.onSaveBefore(service, domain);
        boolean saved = service.save(entity);
        interceptor.onSaveAfter(service, entity, saved);
        return R.ok(saved);
    }

    /**
     * 编辑
     */
    @Operation(summary = "修改")
    @PutMapping("/edit")
    public R<Boolean> edit(@Validated @RequestBody D domain) {
        WebCrudInterceptor<T, D, S> interceptor = getInterceptor();
        T entity = interceptor.onEditBefore(service, domain);
        boolean edited = service.update(entity, Wrappers.<T>query().eq("id", entity.getId()));
        interceptor.onEditAfter(service, entity, edited);
        return R.ok(edited);
    }

    /**
     * 保存或者更新
     */
    @Operation(summary = "保存或修改")
    @PostMapping("/save-or-update")
    public R<Boolean> saveOrUpdate(@Validated @RequestBody D domain) {
        WebCrudInterceptor<T, D, S> interceptor = getInterceptor();
        T entity = interceptor.onSaveOrUpdateBefore(service, domain);
        boolean saved = service.saveOrUpdate(entity, Wrappers.<T>query().eq("id", entity.getId()));
        interceptor.onSaveOrUpdateAfter(service, entity, saved);
        return R.ok(saved);
    }

    /**
     * 批量保存
     */
    @PostMapping("/batch-save-or-update")
    @Operation(summary = "批量保存或更新")
    public R<Boolean> batchSave(@Validated @RequestBody List<D> domain) throws BizException {
        WebCrudInterceptor<T, D, S> interceptor = getInterceptor();
        List<T> entity = interceptor.onBatchSaveBefore(service, domain);
        boolean saved = service.saveOrUpdateBatch(entity);
        interceptor.onBatchSaveAfter(service, entity, saved);
        return R.ok(saved);
    }

    /**
     * 批量删除
     */
    @Operation(summary = "删除")
    @DeleteMapping("/delete")
    public R<Boolean> delete(@RequestBody List<Long> ids) {
        WebCrudInterceptor<T, D, S> interceptor = getInterceptor();
        interceptor.onDeleteBefore(service, ids);
        boolean removed = service.removeByIds(ids);
        interceptor.onDeleteAfter(service, removed);
        return R.ok(removed);
    }

    /**
     * 详情
     */
    @Operation(summary = "详情")
    @GetMapping("/details")
    public R<D> details(long id) {
        WebCrudInterceptor<T, D, S> interceptor = getInterceptor();
        interceptor.onDetailsBefore(service, id);
        T details = service.details(id);
        D domain = interceptor.onDetailsAfter(service, details);
        return R.ok(domain);
    }

    /**
     * 列表
     */
    @PostMapping("/list")
    @Operation(summary = "列表")
    public R<List<D>> list(@RequestBody QueryParam<T> params) throws BizException {
        WebCrudInterceptor<T, D, S> interceptor = getInterceptor();
        interceptor.onListBefore(service, params);
        QueryWrapper<T> queryWrapper = Conditions.query(params, getEntityType());
        List<T> list = service.list(queryWrapper);
        List<D> ds = interceptor.onListAfter(service, list, params);
        return R.ok(ds);
    }

    /**
     * 分页
     */
    @Operation(summary = "分页")
    @PostMapping("/page")
    public R<IPage<D>> page(@RequestBody QueryParam<T> params) {
        WebCrudInterceptor<T, D, S> interceptor = getInterceptor();
        interceptor.onPageBefore(service, params);
        QueryWrapper<T> queryWrapper = Conditions.query(params, getEntityType());
        Page<T> entityPage = service.page(params.getPage(), queryWrapper);
        IPage<D> diPage = interceptor.onPageAfter(service, entityPage, params);
        return R.ok(diPage);
    }

    /**
     * 导出
     */
    @Operation(summary = "导出")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody QueryParam<T> params) {
        WebCrudInterceptor<T, D, S> interceptor = getInterceptor();
        Class<T> clazz = getEntityType();
        QueryWrapper<T> queryWrapper = Conditions.query(params, clazz);
        List<T> list = service.list(queryWrapper);
        List<D> ds = interceptor.onExportBefore(service, list);
        ExcelUtil.export(response, ds, getDomainType());
        interceptor.onExportAfter(service, response, ds);
    }

    /**
     * 导入
     */
    @Operation(summary = "导入")
    @PostMapping("/import")
    public R<Boolean> importFile(MultipartFile file) {
        WebCrudInterceptor<T, D, S> interceptor = getInterceptor();
        interceptor.onImportBefore(service, file);
        ExcelUtil.save(file, service, getEntityType());
        interceptor.onImportAfter(service);
        return ok(Boolean.TRUE);
    }

    /**
     * 获取实体类型
     */
    protected Class<T> getEntityType() {
        return (Class<T>) ReflectTool.getGenericType(this, TurboCrudController.class);
    }

    /**
     * 获取领域类型
     */
    protected Class<D> getDomainType() {
        return (Class<D>) ReflectTool.getGenericType(this, TurboCrudController.class, 1);
    }

    /**
     * 获取Interceptor
     */
    protected <I extends WebCrudInterceptor<T, D, S>> I getInterceptor() {
        return (I) new WebCrudInterceptor<T, D, S>() {
        };
    }
}
