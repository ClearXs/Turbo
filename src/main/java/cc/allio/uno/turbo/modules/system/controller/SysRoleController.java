package cc.allio.uno.turbo.modules.system.controller;

import cc.allio.uno.turbo.common.web.R;
import cc.allio.uno.turbo.common.web.TurboCrudController;
import cc.allio.uno.turbo.common.mybatis.entity.BaseEntity;
import cc.allio.uno.turbo.modules.system.dto.GrantPermissionDTO;
import cc.allio.uno.turbo.modules.system.entity.SysRole;
import cc.allio.uno.turbo.modules.system.service.ISysRoleService;
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
@Tag(name = "角色")
public class SysRoleController extends TurboCrudController<SysRole> {

    private final ISysRoleService roleService;

    @Override
    public R save(@Validated @RequestBody SysRole sysRole) {
        boolean save = roleService.save(sysRole);
        return ok(save);
    }

    @Override
    public R edit(@Validated @RequestBody SysRole sysRole) {
        boolean edit = roleService.update(sysRole, Wrappers.<SysRole>lambdaQuery().eq(SysRole::getId, sysRole.getId()));
        return ok(edit);
    }

    @Override
    public R saveOrUpdate(@Validated @RequestBody SysRole sysRole) {
        boolean edit = roleService.saveOrUpdate(sysRole, Wrappers.<SysRole>lambdaQuery().eq(SysRole::getId, sysRole.getId()));
        return ok(edit);
    }

    @Override
    public R batchSave(List<SysRole> entity) {
        return null;
    }

    @Override
    public R delete(@RequestBody List<Long> ids) {
        boolean removed = roleService.removeByIds(ids);
        return ok(removed);
    }

    @Override
    public R<SysRole> details(long id) {
        SysRole sysRole = roleService.getById(id);
        return ok(sysRole);
    }

    @Override
    public R<List<SysRole>> list(SysRole sysRole) {
        List<SysRole> list = roleService.list(Wrappers.lambdaQuery(sysRole).orderByDesc(BaseEntity::getUpdatedTime));
        return ok(list);
    }

    @Override
    public R<IPage<SysRole>> page(Page page, SysRole sysRole) {
        IPage<SysRole> sysRolePage = roleService.page(page, Wrappers.lambdaQuery(sysRole).orderByDesc(BaseEntity::getUpdatedTime));
        return ok(sysRolePage);
    }

    @PostMapping("/grant")
    @Operation(summary = "授权")
    public R<Boolean> grant(@RequestBody GrantPermissionDTO grantPermission) {
        boolean grant = roleService.grant(grantPermission);
        return ok(grant);
    }
}
