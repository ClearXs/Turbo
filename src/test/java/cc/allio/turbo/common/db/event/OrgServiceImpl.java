package cc.allio.turbo.common.db.event;

import cc.allio.turbo.common.db.entity.Org;

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
