package cc.allio.turbo.modules.developer.api.service;

import cc.allio.uno.core.api.OptionalContext;
import cc.allio.uno.core.function.lambda.ThrowingMethodConsumer;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * {@link DomainAspect}的默认实现
 *
 * @author j.x
 * @date 2024/2/28 19:45
 * @since 0.1.1
 */
@Getter
@AllArgsConstructor
public class DomainInspectImpl implements DomainAspect {

    private final String domainMethod;
    private final ThrowingMethodConsumer<OptionalContext> callback;
}
