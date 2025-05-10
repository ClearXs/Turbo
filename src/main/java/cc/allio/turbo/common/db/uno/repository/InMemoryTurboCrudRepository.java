package cc.allio.turbo.common.db.uno.repository;

import cc.allio.turbo.common.db.entity.Entity;
import cc.allio.turbo.common.domain.DomainEventContext;
import cc.allio.uno.core.bus.EventBus;
import cc.allio.uno.data.orm.executor.AggregateCommandExecutor;

public class InMemoryTurboCrudRepository<T extends Entity> implements ITurboCrudRepository<T> {
    @Override
    public void setDomainEventBus(EventBus<DomainEventContext> eventBus) {

    }

    @Override
    public EventBus<DomainEventContext> getDomainEventBus() {
        return null;
    }

    @Override
    public AggregateCommandExecutor getExecutor() {
        return null;
    }
}
