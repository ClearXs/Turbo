package cc.allio.turbo.common.db.uno.repository.impl;

import cc.allio.turbo.common.db.entity.Entity;
import cc.allio.turbo.common.db.uno.repository.ITurboCrudRepository;
import cc.allio.turbo.common.domain.DomainEventContext;
import cc.allio.uno.core.bus.EventBus;

/**
 * base on abstract class {@link ITurboCrudRepository}
 *
 * @author j.x
 * @date 2024/2/29 23:31
 * @since 0.1.1
 */
public abstract class TurboCrudRepositoryImpl<T extends Entity> implements ITurboCrudRepository<T> {

    private EventBus<DomainEventContext> eventBus;

    @Override
    public EventBus<DomainEventContext> getDomainEventBus() {
        return eventBus;
    }

    @Override
    public void setDomainEventBus(EventBus<DomainEventContext> eventBus) {
        this.eventBus = eventBus;
    }
}
