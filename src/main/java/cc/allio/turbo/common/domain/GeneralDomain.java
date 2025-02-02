package cc.allio.turbo.common.domain;

import cc.allio.uno.core.bus.EventBus;
import lombok.Getter;

/**
 * domain generic type D load.
 *
 * @author j.x
 * @since 0.2.0
 */
public class GeneralDomain<D> implements Domain<D> {

    @Getter
    final D load;
    EventBus<DomainEventContext> eventBus;

    public GeneralDomain(D load, EventBus<DomainEventContext> eventBus) {
        this.load = load;
        this.eventBus = eventBus;
    }


    @Override
    public void setDomainEventBus(EventBus<DomainEventContext> eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public EventBus<DomainEventContext> getDomainEventBus() {
        return eventBus;
    }
}
