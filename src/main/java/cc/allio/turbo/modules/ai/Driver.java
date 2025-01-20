package cc.allio.turbo.modules.ai;

import cc.allio.turbo.common.event.DomainEventBus;
import cc.allio.turbo.common.event.Subscriber;

public class Driver implements Subscriber {

    private DomainEventBus domainEventBus;

    public Driver(DomainEventBus domainEventBus) {
        this.domainEventBus = domainEventBus;
    }

    @Override
    public void setDomainEventBus(DomainEventBus eventBus) {
        this.domainEventBus = eventBus;
    }

    @Override
    public DomainEventBus getDomainEventBus() {
        return this.domainEventBus;
    }

}
