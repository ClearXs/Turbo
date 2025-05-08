package cc.allio.turbo.common.db.uno.repository;

import cc.allio.turbo.common.db.entity.Entity;
import cc.allio.turbo.common.domain.DomainEventContext;
import cc.allio.uno.core.bus.EventBus;

/**
 * memory crud repository service
 *
 * @author j.x
 * @since 0.2.0
 */
public class InMemoryTurboCrudRepositoryService<T extends Entity> implements ITurboCrudRepositoryService<T> {

    @Override
    public void setDomainEventBus(EventBus<DomainEventContext> eventBus) {

    }

    @Override
    public EventBus<DomainEventContext> getDomainEventBus() {
        return null;
    }

    @Override
    public ITurboCrudRepository<T> getRepository() {
        return new InMemoryTurboCrudRepository<>();
    }
}
