package cc.allio.turbo.common.domain;

import cc.allio.uno.core.bus.EventBus;

/**
 * when bean after initialization. will be implementation aware set {@link EventBus}
 *
 * @author j.x
 * @date 2024/8/27 15:28
 * @see DomainBeanPostProcessor
 * @since 0.1.1
 */
public interface DomainEventBusAware {

    /**
     * Set {@link EventBus} of the bean
     *
     * @param eventBus the {@link EventBus} instance.
     */
    void setDomainEventBus(EventBus<DomainEventContext> eventBus);
}
