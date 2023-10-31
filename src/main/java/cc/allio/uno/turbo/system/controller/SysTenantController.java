package cc.allio.uno.turbo.system.controller;

import cc.allio.uno.turbo.common.R;
import cc.allio.uno.turbo.common.TurboController;
import cc.allio.uno.turbo.common.exception.BizException;
import cc.allio.uno.turbo.system.entity.SysTenant;
import cc.allio.uno.turbo.system.entity.SysUser;
import cc.allio.uno.turbo.system.service.ISysTenantService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
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
    public R save(@RequestBody SysTenant sysTenant) throws BizException {
        boolean save = tenantService.save(sysTenant);
        return ok(save);
    }

    @Operation(summary = "批量保存")
    @PostMapping("/batchSave")
    public R batchSave(@RequestBody List<SysTenant> tenants) {
        boolean save = tenantService.saveBatch(tenants);
        return ok(save);
    }

    @Operation(summary = "修改")
    @PutMapping("/edit")
    public R edit(@RequestBody SysTenant sysTenant) {
        boolean edit = tenantService.updateById(sysTenant);
        return ok(edit);
    }

    @Operation(summary = "删除")
    @DeleteMapping("/delete")
    public R delete(long id) {
        boolean removed = tenantService.removeById(id);
        return ok(removed);
    }

    @Operation(summary = "详情")
    @GetMapping("/details")
    public R<SysTenant> details(long id) {
        SysTenant sysTenant = tenantService.getById(id);
        return ok(sysTenant);
    }

    @Operation(summary = "用户列表")
    @GetMapping("/list")
    public R<List<SysTenant>> list(SysTenant sysTenant) {
        List<SysTenant> list = tenantService.list(new QueryWrapper<>(sysTenant));
        return ok(list);
    }

    @Operation(summary = "分页")
    @GetMapping("/page")
    public R<IPage<SysUser>> page(Page page, SysTenant sysTenant) {
        IPage<SysUser> sysUserPage = tenantService.page(page, new QueryWrapper<>(sysTenant));
        return ok(sysUserPage);
    }

}
