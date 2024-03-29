package cc.allio.turbo.modules.message.runtime.targeter;

import cc.allio.turbo.modules.message.config.SendTarget;
import cc.allio.turbo.modules.message.config.Target;
import cc.allio.turbo.modules.message.runtime.RuntimeVariable;

import java.util.List;

/**
 * 发送目标接口
 *
 * @author j.x
 * @date 2024/3/29 00:27
 * @since 0.1.1
 */
public interface Targeter {

    /**
     * 获取发送目标用户集合
     *
     * @param sendTarget      the sendTarget
     * @param runtimeVariable the runtimeVariable
     * @return 列表
     */
    List<Long> getUserList(SendTarget sendTarget, RuntimeVariable runtimeVariable);

    Target getTarget();
}
