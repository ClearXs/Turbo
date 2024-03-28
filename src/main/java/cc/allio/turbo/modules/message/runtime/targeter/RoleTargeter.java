package cc.allio.turbo.modules.message.runtime.targeter;

import cc.allio.turbo.common.db.entity.IdEntity;
import cc.allio.turbo.modules.message.config.SendTarget;
import cc.allio.turbo.modules.message.config.Target;
import cc.allio.turbo.modules.message.runtime.RuntimeVariable;
import cc.allio.turbo.modules.system.entity.SysRole;
import cc.allio.turbo.modules.system.service.ISysRoleService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * role-target
 *
 * @author j.x
 * @date 2024/3/29 00:34
 * @since 0.1.1
 */
@Component
@AllArgsConstructor
public class RoleTargeter implements Targeter {

    private ISysRoleService roleService;

    @Override
    public List<Long> getUserList(SendTarget sendTarget, RuntimeVariable runtimeVariable) {
        List<SysRole> sysRoles = roleService.list(Wrappers.<SysRole>lambdaQuery().eq(SysRole::getCode, sendTarget.getKey()));
        return sysRoles.stream().map(IdEntity::getId).toList();
    }

    @Override
    public Target getTarget() {
        return Target.ROLE;
    }
}
