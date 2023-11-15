package cc.allio.uno.turbo.system.controller;

import cc.allio.uno.turbo.common.R;
import cc.allio.uno.turbo.common.TurboController;
import cc.allio.uno.turbo.common.exception.BizException;
import cc.allio.uno.turbo.system.constant.UserStatus;
import cc.allio.uno.turbo.system.dto.BindingRoleDTO;
import cc.allio.uno.turbo.system.entity.SysUser;
import cc.allio.uno.turbo.system.service.ISysUserService;
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
@RequestMapping("/sys/user")
@AllArgsConstructor
@Tag(name = "用户模块")
public class SysUserController extends TurboController {

    private final ISysUserService sysUserService;

    @PostMapping("/save")
    @Operation(summary = "保存")
    public R save(@Validated @RequestBody SysUser sysUser) throws BizException {
        boolean save = sysUserService.saveUser(sysUser);
        return ok(save);
    }

    @Operation(summary = "批量保存")
    @PostMapping("/batchSave")
    public R batchSave(@Validated @RequestBody List<SysUser> users) {
        boolean save = sysUserService.saveBatch(users);
        return ok(save);
    }

    @Operation(summary = "修改")
    @PutMapping("/edit")
    public R edit(@Validated @RequestBody SysUser sysUser) {
        boolean edit =
                sysUserService.update(
                        sysUser,
                        Wrappers.<SysUser>lambdaQuery().eq(SysUser::getId, sysUser.getId()));
        return ok(edit);
    }

    @Operation(summary = "保存或修改")
    @PutMapping("/save-or-update")
    public R saveOrUpdate(@Validated @RequestBody SysUser sysUser) {
        boolean edit =
                sysUserService.saveOrUpdate(
                        sysUser,
                        Wrappers.<SysUser>lambdaQuery().eq(SysUser::getId, sysUser.getId()));
        return ok(edit);
    }

    @Operation(summary = "删除")
    @DeleteMapping("/delete")
    public R delete(@RequestBody List<Long> ids) {
        boolean removed = sysUserService.removeByIds(ids);
        return ok(removed);
    }

    @Operation(summary = "详情")
    @GetMapping("/details")
    public R<SysUser> details(long id) {
        SysUser sysUser = sysUserService.getById(id);
        return ok(sysUser);
    }

    @Operation(summary = "列表")
    @GetMapping("/list")
    public R<List<SysUser>> list(SysUser sysUser) {
        List<SysUser> list = sysUserService.list(new QueryWrapper<>(sysUser));
        return ok(list);
    }

    @Operation(summary = "分页")
    @GetMapping("/page")
    public R<IPage<SysUser>> page(Page page, SysUser sysUser) {
        IPage<SysUser> sysUserPage = sysUserService.page(page, new QueryWrapper<>(sysUser));
        return ok(sysUserPage);
    }

    @Operation(summary = "锁定")
    @GetMapping("/lock")
    public R<Boolean> lock(long id) {
        boolean locked =
                sysUserService.update(
                        Wrappers.<SysUser>lambdaUpdate().set(SysUser::getStatus, UserStatus.LOCK).eq(SysUser::getId, id));
        return ok(locked);
    }

    @Operation(summary = "激活")
    @GetMapping("/active")
    public R<Boolean> active(long id) {
        boolean locked =
                sysUserService.update(
                        Wrappers.<SysUser>lambdaUpdate().set(SysUser::getStatus, UserStatus.ENABLE).eq(SysUser::getId, id));
        return ok(locked);
    }

    @Operation(summary = "绑定角色")
    @PostMapping("/binding-roles")
    public R<Boolean> bindingRoles(@RequestBody @Validated BindingRoleDTO bindingRole) {
        Boolean bindinged = sysUserService.bindingRoles(bindingRole);
        return ok(bindinged);
    }
}
