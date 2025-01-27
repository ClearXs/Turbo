package cc.allio.turbo.common.domain;

import cc.allio.uno.core.bus.EventBus;

/**
 * the bean get {@link EventBus}
 *
 * @author j.x
 * @date 2024/8/27 15:24
 * @since 0.1.1
 * @see DomainEventBusAware
 */
public interface DomainEventBusGetter {

    /**
     * get {@link EventBus}
     */
    EventBus<DomainEventContext> getDomainEventBus();
}
