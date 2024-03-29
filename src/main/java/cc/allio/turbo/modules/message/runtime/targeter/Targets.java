package cc.allio.turbo.modules.message.runtime.targeter;

import cc.allio.turbo.modules.message.config.SendTarget;
import cc.allio.turbo.modules.message.config.Target;
import cc.allio.turbo.modules.message.runtime.RuntimeVariable;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class Targets implements InitializingBean {

    private static final Map<Target, Targeter> targeterMap = Maps.newHashMap();

    private final ObjectProvider<List<Targeter>> listableTarget;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<Targeter> targeters = listableTarget.getIfAvailable(Collections::emptyList);
        for (Targeter targeter : targeters) {
            targeterMap.put(targeter.getTarget(), targeter);
        }
    }

    /**
     * get send target user by {@link SendTarget} and {@link RuntimeVariable}
     *
     * @param sendTarget      the sendTarget
     * @param runtimeVariable the runtimeVariable
     * @return list user id
     */
    public static List<Long> getTargetUser(SendTarget sendTarget, RuntimeVariable runtimeVariable) {
        Target target = sendTarget.getTarget();
        Targeter targeter = targeterMap.get(target);
        if (targeter != null) {
            return targeter.getUserList(sendTarget, runtimeVariable);
        }
        return Collections.emptyList();
    }
}
