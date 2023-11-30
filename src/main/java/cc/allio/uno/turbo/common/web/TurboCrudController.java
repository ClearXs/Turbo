package cc.allio.uno.turbo.common.web;

import cc.allio.uno.turbo.common.exception.BizException;
import cc.allio.uno.turbo.common.mybatis.help.Conditions;
import cc.allio.uno.turbo.common.mybatis.entity.IdEntity;
import cc.allio.uno.turbo.common.web.params.QueryParam;
import cc.allio.uno.turbo.common.mybatis.service.ITurboCrudService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 定义curd接口
 *
 * @author j.x
 * @date 2023/11/16 18:10
 * @since 1.0.0
 */
public abstract class TurboCrudController<T extends IdEntity, S extends ITurboCrudService<T>> extends TurboController {

    @Autowired
    protected S service;

    /**
     * 保存
     */
    @PostMapping("/save")
    @Operation(summary = "保存")
    protected R<Boolean> save(@Validated @RequestBody T entity) {
        boolean save = service.save(entity);
        return ok(save);
    }

    /**
     * 编辑
     */
    @Operation(summary = "修改")
    @PutMapping("/edit")
    public R<Boolean> edit(@Validated @RequestBody T entity) {
        boolean edit = service.update(entity, Wrappers.<T>query().eq("id", entity.getId()));
        return ok(edit);
    }

    /**
     * 保存或者更新
     */
    @Operation(summary = "保存或修改")
    @PostMapping("/save-or-update")
    public R<Boolean> saveOrUpdate(@Validated @RequestBody T entity) {
        boolean edit = service.saveOrUpdate(entity, Wrappers.<T>query().eq("id", entity.getId()));
        return ok(edit);
    }

    /**
     * 批量保存
     */
    @PostMapping("/batchSave")
    @Operation(summary = "批量保存")
    public R<Boolean> batchSave(@Validated @RequestBody List<T> entity) {
        boolean batch = service.saveBatch(entity);
        return ok(batch);
    }

    /**
     * 批量删除
     */
    @Operation(summary = "删除")
    @DeleteMapping("/delete")
    public R<Boolean> delete(@RequestBody List<Long> ids) {
        boolean removed = service.removeByIds(ids);
        return ok(removed);
    }

    /**
     * 详情
     */
    @Operation(summary = "详情")
    @GetMapping("/details")
    public R<T> details(long id) {
        T entity = service.getById(id);
        return ok(entity);
    }

    /**
     * 列表
     */
    @PostMapping("/list")
    @Operation(summary = "列表")
    public R<List<T>>  list(@RequestBody QueryParam<T> params) throws BizException {
        QueryWrapper<T> queryWrapper = Conditions.query(params, getEntityType());
        List<T> list = service.list(queryWrapper);
        return ok(list);
    }

    /**
     * 分页
     */
    @Operation(summary = "分页")
    @PostMapping("/page")
    public R<IPage<T>> page(@RequestBody QueryParam<T> params) {
        QueryWrapper<T> queryWrapper = Conditions.query(params, getEntityType());
        Page<T> entityPage = service.page(params.getPage(), queryWrapper);
        return ok(entityPage);
    }

    /**
     * 获取实体类型
     */
    protected Class<T> getEntityType() {
        return (Class<T>) ReflectionKit.getSuperClassGenericType(this.getClass(), TurboCrudController.class, 0);
    }

    protected S getService() {
        return service;
    }
}
