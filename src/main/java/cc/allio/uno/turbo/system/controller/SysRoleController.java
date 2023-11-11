package cc.allio.uno.turbo.system.controller;

import cc.allio.uno.turbo.common.R;
import cc.allio.uno.turbo.common.TurboController;
import cc.allio.uno.turbo.system.dto.GrantPermissionDTO;
import cc.allio.uno.turbo.system.entity.SysRole;
import cc.allio.uno.turbo.system.service.ISysRoleService;
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
@RequestMapping("/sys/role")
@AllArgsConstructor
@Tag(name = "角色模块")
public class SysRoleController extends TurboController {

    private final ISysRoleService roleService;

    @PostMapping("/save")
    @Operation(summary = "保存")
    public R save(@Validated @RequestBody SysRole sysRole) {
        boolean save = roleService.save(sysRole);
        return ok(save);
    }

    @PutMapping("/edit")
    @Operation(summary = "修改")
    public R edit(@Validated @RequestBody SysRole sysRole) {
        boolean edit = roleService.update(sysRole, Wrappers.<SysRole>lambdaQuery().ge(SysRole::getId, sysRole.getId()));
        return ok(edit);
    }

    @Operation(summary = "删除")
    @DeleteMapping("/delete")
    public R delete(long id) {
        boolean removed = roleService.removeById(id);
        return ok(removed);
    }

    @GetMapping("/details")
    @Operation(summary = "详情")
    public R<SysRole> details(long id) {
        SysRole sysRole = roleService.getById(id);
        return ok(sysRole);
    }

    @GetMapping("/list")
    @Operation(summary = "列表")
    public R<List<SysRole>> list(SysRole sysRole) {
        List<SysRole> list = roleService.list(new QueryWrapper<>(sysRole));
        return ok(list);
    }

    @GetMapping("/page")
    @Operation(summary = "分页")
    public R<IPage<SysRole>> page(Page page, SysRole sysRole) {
        IPage<SysRole> sysRolePage = roleService.page(page, new QueryWrapper<>(sysRole));
        return ok(sysRolePage);
    }

    @PostMapping("/grant")
    @Operation(summary = "授权")
    public R<Boolean> grant(@RequestBody GrantPermissionDTO grantPermission) {
        boolean grant = roleService.grant(grantPermission);
        return ok(grant);
    }
}
