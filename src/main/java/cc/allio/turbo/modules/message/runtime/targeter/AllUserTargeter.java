package cc.allio.turbo.modules.message.runtime.targeter;

import cc.allio.turbo.common.db.entity.IdEntity;
import cc.allio.turbo.modules.message.config.SendTarget;
import cc.allio.turbo.modules.message.config.Target;
import cc.allio.turbo.modules.message.runtime.RuntimeVariable;
import cc.allio.turbo.modules.system.entity.SysUser;
import cc.allio.turbo.modules.system.service.ISysUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * all-user
 *
 * @author j.x
 * @date 2024/3/29 00:49
 * @since 0.1.1
 */
@Component
@AllArgsConstructor
public class AllUserTargeter implements Targeter {

    private final ISysUserService sysUserService;

    @Override
    public List<Long> getUserList(SendTarget sendTarget, RuntimeVariable runtimeVariable) {
        List<SysUser> users = sysUserService.list();
        return users.stream().map(IdEntity::getId).toList();
    }

    @Override
    public Target getTarget() {
        return Target.ALL_USER;
    }
}
