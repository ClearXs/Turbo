package cc.allio.turbo.modules.developer.api.service;

import cc.allio.turbo.common.event.DomainEventBus;
import cc.allio.turbo.modules.developer.api.annotation.Domain;

@Domain("user")
public class UserDomainServiceImpl implements IUserDomainService {

    private DomainEventBus eventBus;

    @Override
    public void setDomainEventBus(DomainEventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public DomainEventBus getDomainEventBus() {
        return eventBus;
    }
}
