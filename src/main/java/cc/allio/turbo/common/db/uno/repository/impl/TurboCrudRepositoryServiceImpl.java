package cc.allio.turbo.common.db.uno.repository.impl;

import cc.allio.turbo.common.db.entity.Entity;
import cc.allio.turbo.common.event.DomainEventBus;
import cc.allio.turbo.common.db.uno.repository.DSExtractor;
import cc.allio.turbo.common.db.uno.repository.ITurboCrudRepository;
import cc.allio.turbo.common.db.uno.repository.ITurboCrudRepositoryService;
import cc.allio.uno.core.reflect.ReflectTools;
import cc.allio.uno.data.orm.executor.AggregateCommandExecutor;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * base on abstract class {@link ITurboCrudRepositoryService}
 *
 * @author j.x
 * @date 2024/2/29 23:29
 * @since 0.1.1
 */
public abstract class TurboCrudRepositoryServiceImpl<T extends Entity> implements ITurboCrudRepositoryService<T> {

    private final ITurboCrudRepository<T> repository;
    private Class<T> entityClass;

    protected TurboCrudRepositoryServiceImpl(AggregateCommandExecutor commandExecutor) {
        this(commandExecutor, null);
    }

    protected TurboCrudRepositoryServiceImpl(AggregateCommandExecutor commandExecutor, Class<T> entityClass) {
        Supplier<AggregateCommandExecutor> commandExecutorSupplier =
                Optional.ofNullable(commandExecutor)
                        .<Supplier<AggregateCommandExecutor>>map(c -> () -> c)
                        .orElse(() -> DSExtractor.extract(this.getClass()));
        Supplier<Class<T>> entityClassSupplier =
                Optional.ofNullable(entityClass)
                        .<Supplier<Class<T>>>map(e -> () -> e)
                        .orElse(this::getEntityClass);
        this.repository = new SimpleTurboCurdRepositoryImpl<>(commandExecutorSupplier, entityClassSupplier);
        this.entityClass = entityClass;
    }

    @Override
    public ITurboCrudRepository<T> getRepository() {
        return repository;
    }

    @Override
    public AggregateCommandExecutor getExecutor() {
        return getRepository().getExecutor();
    }

    @Override
    public Class<T> getEntityClass() {
        if (entityClass == null) {
            return (Class<T>) ReflectTools.getGenericType(this, TurboCrudRepositoryServiceImpl.class, 0);
        }
        return entityClass;
    }

    @Override
    public void setEntityType(Class<T> entityType) {
        this.entityClass = entityType;
    }

    @Override
    public void setDomainEventBus(DomainEventBus eventBus) {
        repository.setDomainEventBus(eventBus);
    }

    @Override
    public DomainEventBus getDomainEventBus() {
        return repository.getDomainEventBus();
    }
}
