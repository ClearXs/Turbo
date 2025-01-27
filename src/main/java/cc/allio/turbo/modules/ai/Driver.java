package cc.allio.turbo.modules.ai;

import cc.allio.turbo.common.domain.DomainEventContext;
import cc.allio.turbo.common.domain.SubPuber;
import cc.allio.uno.core.bus.EventBus;

public class Driver implements SubPuber<Input> {

    private EventBus<DomainEventContext> eventBus;

    public Driver(EventBus<DomainEventContext> eventBus) {
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
