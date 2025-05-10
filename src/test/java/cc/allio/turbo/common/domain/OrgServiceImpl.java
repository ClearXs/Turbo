package cc.allio.turbo.common.domain;

import cc.allio.turbo.common.db.entity.Org;
import cc.allio.uno.core.bus.EventBus;

public class OrgServiceImpl implements Subscriber<Org> {

    private EventBus<DomainEventContext> eventBus;

    public String getName() {
        return "org";
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
