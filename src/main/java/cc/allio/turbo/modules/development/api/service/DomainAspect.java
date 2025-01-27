package cc.allio.turbo.modules.development.api.service;

import cc.allio.uno.core.function.lambda.ThrowingMethodConsumer;
import cc.allio.uno.core.util.map.OptionalMap;

/**
 * 领域对象切面
 *
 * @author j.x
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
    ThrowingMethodConsumer<OptionalMap<String>> getCallback();
}
