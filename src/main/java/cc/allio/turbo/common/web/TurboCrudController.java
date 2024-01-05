package cc.allio.turbo.common.web;

import cc.allio.turbo.common.excel.util.ExcelUtil;
import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.common.mybatis.entity.IdEntity;
import cc.allio.turbo.common.mybatis.help.Conditions;
import cc.allio.turbo.common.mybatis.service.ITurboCrudService;
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
 * @author j.x
 * @date 2023/11/16 18:10
 * @since 0.1.0
 * @param <T> 实体结构
 * @param <S> 实体对应service类型
 * @param <V> 复合类型（或者成为VO）
 */
@Getter
public abstract class TurboCrudController<T extends IdEntity, S extends ITurboCrudService<T>, V extends T> extends TurboController {

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
    public R<V> details(long id) {
        V entity = service.details(id);
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
     * 导出
     */
    @Operation(summary = "导出")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody QueryParam<T> params) {
        Class<T> clazz = getEntityType();
        QueryWrapper<T> queryWrapper = Conditions.query(params, clazz);
        List<T> list = service.list(queryWrapper);
        ExcelUtil.export(response, list, clazz);
    }

    /**
     * 导入
     */
    @Operation(summary = "导入")
    @PostMapping("/import")
    public R<Boolean> importFile(MultipartFile file) {
        ExcelUtil.save(file, service, getEntityType());
        return ok(Boolean.TRUE);
    }

    /**
     * 获取实体类型
     */
    protected Class<T> getEntityType() {
        return (Class<T>) ReflectTool.getGenericType(this, TurboCrudController.class);
    }
}
