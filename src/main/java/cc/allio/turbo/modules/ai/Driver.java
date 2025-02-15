package cc.allio.turbo.modules.ai;

import cc.allio.turbo.common.domain.DomainEventContext;
import cc.allio.turbo.common.domain.SubPuber;
import cc.allio.uno.core.bus.EventBus;
import cc.allio.uno.core.bus.EventBusFactory;

/**
 * AI Agent driver
 *
 * @author j.x
 * @since 0.2.0
 */
public class Driver<D> implements SubPuber<D> {

    private EventBus<DomainEventContext> eventBus;
    private final Class<D> inherentClass;

    public Driver(Class<D> inherentClass, EventBus<DomainEventContext> eventBus) {
        this.eventBus = eventBus;
        this.inherentClass = inherentClass;
    }

    @Override
    public void setDomainEventBus(EventBus<DomainEventContext> eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public EventBus<DomainEventContext> getDomainEventBus() {
        return eventBus;
    }

    /**
     * 获取订阅者名称
     *
     * @return name
     */
    public String getDomainName() {
        return inherentClass.getSimpleName();
    }

    /**
     * 获取领域类型
     */
    public Class<D> getDomainType() {
        return inherentClass;
    }

    /**
     * @see #from(Class, EventBus)
     */
    public static <D> Driver<D> from(Class<D> domainClass) {
        return from(domainClass, EventBusFactory.current());
    }

    /**
     * create the {@link Driver} instance
     *
     * @param domainClass the domain class
     * @param <D>         the domain type
     * @param eventBus    the event bus instance
     * @return
     */
    public static <D> Driver<D> from(Class<D> domainClass, EventBus<DomainEventContext> eventBus) {
        return new Driver<>(domainClass, eventBus);
    }
}
