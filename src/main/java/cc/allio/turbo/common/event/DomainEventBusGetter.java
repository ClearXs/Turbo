package cc.allio.turbo.common.event;

/**
 * the bean get {@link DomainEventBus}
 *
 * @author j.x
 * @date 2024/8/27 15:24
 * @since 0.1.1
 * @see DomainEventBusAware
 */
public interface DomainEventBusGetter {

    /**
     * get {@link DomainEventBus}
     */
    DomainEventBus getDomainEventBus();
}
