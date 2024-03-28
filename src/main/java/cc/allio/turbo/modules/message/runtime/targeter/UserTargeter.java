package cc.allio.turbo.modules.message.runtime.targeter;

import cc.allio.turbo.modules.message.config.SendTarget;
import cc.allio.turbo.modules.message.config.Target;
import cc.allio.turbo.modules.message.runtime.RuntimeVariable;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * user-target
 *
 * @author j.x
 * @date 2024/3/29 00:35
 * @since 0.1.1
 */
@Component
@AllArgsConstructor
public class UserTargeter implements Targeter {

    @Override
    public List<Long> getUserList(SendTarget sendTarget, RuntimeVariable runtimeVariable) {
        return List.of(Long.valueOf(sendTarget.getKey()));
    }

    @Override
    public Target getTarget() {
        return Target.USER;
    }
}
