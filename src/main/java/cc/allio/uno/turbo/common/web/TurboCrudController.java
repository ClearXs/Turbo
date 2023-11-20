package cc.allio.uno.turbo.common.web;

import cc.allio.uno.turbo.common.exception.BizException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
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
public abstract class TurboCrudController<T> extends TurboController {

    /**
     * 保存
     */
    @PostMapping("/save")
    @Operation(summary = "保存")
    protected abstract R save(@Validated @RequestBody T entity) throws BizException;

    /**
     * 编辑
     */
    @Operation(summary = "修改")
    @PutMapping("/edit")
    public abstract R edit(@Validated @RequestBody T entity);

    /**
     * 保存或者更新
     */
    @Operation(summary = "保存或修改")
    @PutMapping("/save-or-update")
    public abstract R saveOrUpdate(@Validated @RequestBody T entity);

    /**
     * 批量保存
     */
    @PostMapping("/batchSave")
    @Operation(summary = "批量保存")
    public abstract R batchSave(@Validated @RequestBody List<T> entity);

    /**
     * 批量删除
     */
    @Operation(summary = "删除")
    @DeleteMapping("/delete")
    public abstract R delete(@RequestBody List<Long> ids) throws BizException;

    /**
     * 详情
     */
    @Operation(summary = "详情")
    @GetMapping("/details")
    public abstract R<T> details(long id);

    /**
     * 列表
     */

    @GetMapping("/list")
    @Operation(summary = "列表")
    public abstract R<List<T>> list(T entity);

    /**
     * 分页
     */
    @Operation(summary = "分页")
    @GetMapping("/page")
    public abstract R<IPage<T>> page(Page page, T entity);

}
