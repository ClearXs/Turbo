package cc.allio.uno.turbo.system.controller;

import cc.allio.uno.turbo.common.R;
import cc.allio.uno.turbo.common.TurboController;
import cc.allio.uno.turbo.system.entity.SysTenant;
import cc.allio.uno.turbo.system.entity.SysUser;
import cc.allio.uno.turbo.system.service.ISysTenantService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sys/tenant")
@AllArgsConstructor
@Tag(name = "租户模块")
public class SysTenantController extends TurboController {

    private final ISysTenantService tenantService;

    @PostMapping("/save")
    @Operation(summary = "保存")
    public R save(@Validated @RequestBody SysTenant sysTenant) {
        boolean save = tenantService.save(sysTenant);
        return ok(save);
    }

    @PostMapping("/batchSave")
    @Operation(summary = "批量保存")
    public R batchSave(@Validated @RequestBody List<SysTenant> tenants) {
        boolean save = tenantService.saveBatch(tenants);
        return ok(save);
    }

    @PutMapping("/edit")
    @Operation(summary = "修改")
    public R edit(@Validated @RequestBody SysTenant sysTenant) {
        boolean edit =
                tenantService.update(
                        sysTenant,
                        Wrappers.<SysTenant>lambdaQuery().ge(SysTenant::getTenantId, sysTenant.getTenantId()));
        return ok(edit);
    }

    @PutMapping("/save-or-update")
    @Operation(summary = "修改")
    public R saveOrUpdate(@Validated @RequestBody SysTenant sysTenant) {
        boolean edit =
                tenantService.saveOrUpdate(
                        sysTenant,
                        Wrappers.<SysTenant>lambdaQuery().ge(SysTenant::getTenantId, sysTenant.getTenantId()));
        return ok(edit);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除")
    public R delete(@RequestBody List<Long> ids) {
        boolean removed = tenantService.removeByIds(ids);
        return ok(removed);
    }

    @GetMapping("/details")
    @Operation(summary = "详情")
    public R<SysTenant> details(long id) {
        SysTenant sysTenant = tenantService.getById(id);
        return ok(sysTenant);
    }

    @GetMapping("/list")
    @Operation(summary = "列表")
    public R<List<SysTenant>> list(SysTenant sysTenant) {
        List<SysTenant> list = tenantService.list(new QueryWrapper<>(sysTenant));
        return ok(list);
    }

    @GetMapping("/page")
    @Operation(summary = "分页")
    public R<IPage<SysUser>> page(Page page, SysTenant sysTenant) {
        IPage<SysUser> sysUserPage = tenantService.page(page, new QueryWrapper<>(sysTenant));
        return ok(sysUserPage);
    }

}
