package cc.allio.turbo.common.event;

/**
 * when bean after initialization. will be implementation aware set {@link DomainEventBus}
 *
 * @author j.x
 * @date 2024/8/27 15:28
 * @since 0.1.1
 * @see DomainBeanPostProcessor
 */
public interface DomainEventBusAware {

    /**
     * Set {@link DomainEventBus} of the bean
     *
     * @param eventBus the {@link DomainEventBus} instance.
     */
    void setDomainEventBus(DomainEventBus eventBus);
}
