package cc.allio.turbo.modules.developer.api.service;

import cc.allio.uno.core.api.OptionalContext;
import cc.allio.uno.core.function.lambda.ThrowingMethodConsumer;

/**
 * 领域对象切面
 *
 * @author jiangwei
 * @date 2024/2/28 19:05
 * @since 0.1.1
 */
public interface DomainAspect {

    /**
     * 获取领域行为
     */
    String getDomainMethod();

    /**
     * 获取领域行为回调
     */
    ThrowingMethodConsumer<OptionalContext> getCallback();
}
