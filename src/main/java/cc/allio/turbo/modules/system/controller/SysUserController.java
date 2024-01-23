package cc.allio.turbo.modules.system.controller;

import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.common.web.R;
import cc.allio.turbo.common.web.TurboCrudController;
import cc.allio.turbo.modules.system.constant.UserStatus;
import cc.allio.turbo.modules.system.dto.BindingOrgDTO;
import cc.allio.turbo.modules.system.dto.BindingPostDTO;
import cc.allio.turbo.modules.system.dto.BindingRoleDTO;
import cc.allio.turbo.modules.system.dto.ChangePasswordDTO;
import cc.allio.turbo.modules.system.entity.SysUser;
import cc.allio.turbo.modules.system.service.ISysUserService;
import cc.allio.turbo.modules.system.domain.SysUserVO;
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
public class SysUserController extends TurboCrudController<SysUser, SysUserVO, ISysUserService> {


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
    @PostMapping("/binding-role")
    public R<Boolean> bindingRoles(@RequestBody @Validated BindingRoleDTO bindingRole) {
        Boolean bindinged = getService().bindingRole(bindingRole);
        return ok(bindinged);
    }

    @Operation(summary = "绑定组织")
    @PostMapping("/binding-org")
    public R<Boolean> bingdingOrgs(@RequestBody @Validated BindingOrgDTO bindingOrg) {
        Boolean bindinged = getService().bindingOrg(bindingOrg);
        return ok(bindinged);
    }

    @Operation(summary = "绑定岗位")
    @PostMapping("/binding-post")
    public R<Boolean> bingdingOrgs(@RequestBody @Validated BindingPostDTO bindingPost) {
        Boolean bindinged = getService().bindingPost(bindingPost);
        return ok(bindinged);
    }

    @Operation(summary = "更改密码")
    @PostMapping("/change-password")
    public R<Boolean> changePassword(@RequestBody @Validated ChangePasswordDTO changePassword) throws BizException {
        Boolean changed = getService().changePassword(changePassword.getId(), changePassword.getNewPassword());
        return ok(changed);
    }
}
