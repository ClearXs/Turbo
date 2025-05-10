package cc.allio.turbo.modules.development.api.service;

import cc.allio.uno.core.function.lambda.ThrowingMethodConsumer;
import cc.allio.uno.core.util.map.OptionalMap;
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
    private final ThrowingMethodConsumer<OptionalMap<String>> callback;
}
