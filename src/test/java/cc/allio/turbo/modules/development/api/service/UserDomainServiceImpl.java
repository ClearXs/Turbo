package cc.allio.turbo.modules.development.api.service;

import cc.allio.turbo.common.domain.DomainEventContext;
import cc.allio.turbo.modules.development.api.annotation.Domain;
import cc.allio.uno.core.bus.EventBus;

@Domain("user")
public class UserDomainServiceImpl implements IUserDomainService {

    private EventBus<DomainEventContext> eventBus;

    @Override
    public void setDomainEventBus(EventBus<DomainEventContext> eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public EventBus<DomainEventContext> getDomainEventBus() {
        return eventBus;
    }
}
