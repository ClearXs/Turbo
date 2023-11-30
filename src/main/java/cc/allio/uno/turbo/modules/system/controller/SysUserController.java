package cc.allio.uno.turbo.modules.system.controller;

import cc.allio.uno.turbo.common.exception.BizException;
import cc.allio.uno.turbo.common.web.R;
import cc.allio.uno.turbo.common.web.TurboCrudController;
import cc.allio.uno.turbo.common.web.params.QueryParam;
import cc.allio.uno.turbo.modules.system.constant.UserStatus;
import cc.allio.uno.turbo.modules.system.dto.BindingOrgDTO;
import cc.allio.uno.turbo.modules.system.dto.BindingRoleDTO;
import cc.allio.uno.turbo.modules.system.entity.SysUser;
import cc.allio.uno.turbo.modules.system.service.ISysUserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sys/user")
@AllArgsConstructor
@Tag(name = "用户")
public class SysUserController extends TurboCrudController<SysUser, ISysUserService> {

    @Override
    public R<List<SysUser>> list(@RequestBody QueryParam<SysUser> params) throws BizException {
        List<SysUser> list = getService().findList(params);
        return ok(list);
    }

    @Override
    public R<IPage<SysUser>> page(@RequestBody QueryParam<SysUser> params) {
        IPage<SysUser> page = getService().findPage(params);
        return ok(page);
    }

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

    @Operation(summary = "绑定组织")
    @PostMapping("/binding-orgs")
    public R<Boolean> bingdingOrgs(@RequestBody @Validated BindingOrgDTO bindingOrg) {
        Boolean bindinged = getService().bingdingOrgs(bindingOrg);
        return ok(bindinged);
    }

    @Operation(summary = "未绑定组织用户")
    @PostMapping("unbound-org-users")
    public R<List<SysUser>> unboundOrgUser() {
        List<SysUser> unboundOrgUsers = getService().unboundOrgUser();
        return ok(unboundOrgUsers);
    }

    @Operation(summary = "更改密码")
    @PostMapping("/change-password")
    public R<Boolean> changePassword(@RequestBody Long id, @Validated @Min(6) String newPassword) {
        Boolean changed = getService().changePassword(id, newPassword);
        return ok(changed);
    }
}
