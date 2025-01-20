package cc.allio.turbo.common.event;

import cc.allio.turbo.common.db.entity.Org;
import cc.allio.turbo.common.event.DomainEventBus;
import cc.allio.turbo.common.event.Subscriber;

public class OrgServiceImpl implements Subscriber<Org> {

    private DomainEventBus eventBus;

    public String getName() {
        return "org";
    }

    @Override
    public void setDomainEventBus(DomainEventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public DomainEventBus getDomainEventBus() {
        return eventBus;
    }
}
