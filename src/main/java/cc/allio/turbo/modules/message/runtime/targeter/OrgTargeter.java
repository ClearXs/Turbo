package cc.allio.turbo.modules.message.runtime.targeter;

import cc.allio.turbo.common.db.entity.IdEntity;
import cc.allio.turbo.modules.message.config.SendTarget;
import cc.allio.turbo.modules.message.config.Target;
import cc.allio.turbo.modules.message.runtime.RuntimeVariable;
import cc.allio.turbo.modules.system.entity.SysOrg;
import cc.allio.turbo.modules.system.service.ISysOrgService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * org-target
 *
 * @author j.x
 * @date 2024/3/29 00:30
 * @since 0.1.1
 */
@Component
@AllArgsConstructor
public class OrgTargeter implements Targeter {

    private final ISysOrgService sysOrgService;

    @Override
    public List<Long> getUserList(SendTarget sendTarget, RuntimeVariable runtimeVariable) {
        List<SysOrg> sysOrgs = sysOrgService.list(Wrappers.<SysOrg>lambdaQuery().eq(SysOrg::getCode, sendTarget.getKey()));
        return sysOrgs.stream().map(IdEntity::getId).toList();
    }

    @Override
    public Target getTarget() {
        return Target.ORG;
    }
}
