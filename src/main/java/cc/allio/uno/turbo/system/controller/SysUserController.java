package cc.allio.uno.turbo.system.controller;

import cc.allio.uno.turbo.common.R;
import cc.allio.uno.turbo.common.TurboController;
import cc.allio.uno.turbo.common.exception.BizException;
import cc.allio.uno.turbo.system.entity.SysUser;
import cc.allio.uno.turbo.system.service.ISysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sys/user")
@AllArgsConstructor
@Tag(name = "用户模块")
public class SysUserController extends TurboController {

    private final ISysUserService sysUserService;

    @PostMapping("/save")
    @Operation(summary = "保存用户")
    public R save(@RequestBody SysUser sysUser) throws BizException {
        boolean save = sysUserService.saveUser(sysUser);
        return ok(save);
    }

    @Operation(summary = "批量保存用户")
    @PostMapping("/batchSave")
    public R batchSave(List<SysUser> users) {
        boolean save = sysUserService.saveBatch(users);
        return ok(save);
    }

    @Operation(summary = "修改用户")
    @PutMapping("/edit")
    public R edit(SysUser sysUser) {
        boolean edit = sysUserService.updateById(sysUser);
        return ok(edit);
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/delete")
    public R delete(long id) {
        boolean removed = sysUserService.removeById(id);
        return ok(removed);
    }

    @Operation(summary = "用户详情")
    @GetMapping("/details")
    public R<SysUser> details(long id) {
        SysUser sysUser = sysUserService.getById(id);
        return ok(sysUser);
    }

    @Operation(summary = "用户列表")
    @GetMapping("/list")
    @PreAuthorize("hasRole('role')")
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
}
