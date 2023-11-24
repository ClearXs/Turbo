package cc.allio.uno.turbo.modules.system.controller;

import cc.allio.uno.turbo.common.web.R;
import cc.allio.uno.turbo.common.web.TurboCrudController;
import cc.allio.uno.turbo.modules.system.constant.UserStatus;
import cc.allio.uno.turbo.modules.system.dto.BindingRoleDTO;
import cc.allio.uno.turbo.modules.system.entity.SysUser;
import cc.allio.uno.turbo.modules.system.service.ISysUserService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sys/user")
@AllArgsConstructor
@Tag(name = "用户")
public class SysUserController extends TurboCrudController<SysUser, ISysUserService> {


    @Operation(summary = "锁定")
    @GetMapping("/lock")
    public R<Boolean> lock(long id) {
        boolean locked =
                getService().update(
                        Wrappers.<SysUser>lambdaUpdate().set(SysUser::getStatus, UserStatus.LOCK).eq(SysUser::getId, id));
        return ok(locked);
    }

    @Operation(summary = "激活")
    @GetMapping("/active")
    public R<Boolean> active(long id) {
        boolean locked =
                getService().update(
                        Wrappers.<SysUser>lambdaUpdate().set(SysUser::getStatus, UserStatus.ENABLE).eq(SysUser::getId, id));
        return ok(locked);
    }

    @Operation(summary = "绑定角色")
    @PostMapping("/binding-roles")
    public R<Boolean> bindingRoles(@RequestBody @Validated BindingRoleDTO bindingRole) {
        Boolean bindinged = getService().bindingRoles(bindingRole);
        return ok(bindinged);
    }

}
