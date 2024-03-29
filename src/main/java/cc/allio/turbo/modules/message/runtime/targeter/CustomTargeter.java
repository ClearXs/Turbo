package cc.allio.turbo.modules.message.runtime.targeter;

import cc.allio.turbo.modules.message.config.SendTarget;
import cc.allio.turbo.modules.message.config.Target;
import cc.allio.turbo.modules.message.runtime.PlaceholderRuntimeText;
import cc.allio.turbo.modules.message.runtime.RuntimeText;
import cc.allio.turbo.modules.message.runtime.RuntimeVariable;
import cc.allio.uno.core.StringPool;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * custom-target
 *
 * @author j.x
 * @date 2024/3/29 00:29
 * @since 0.1.1
 */
@Component
@AllArgsConstructor
public class CustomTargeter implements Targeter {

    @Override
    public List<Long> getUserList(SendTarget sendTarget, RuntimeVariable runtimeVariable) {
        // 模板变量读取
        String key = sendTarget.getKey();
        RuntimeText runtimeText = new PlaceholderRuntimeText(key, runtimeVariable);
        String maybeUserId = runtimeText.runThenText();
        return Arrays.stream(maybeUserId.split(StringPool.COMMA)).map(Long::valueOf).toList();
    }

    @Override
    public Target getTarget() {
        return Target.CUSTOM;
    }
}
