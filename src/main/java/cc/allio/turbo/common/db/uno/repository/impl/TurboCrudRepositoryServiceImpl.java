package cc.allio.turbo.common.db.uno.repository.impl;

import cc.allio.turbo.common.db.entity.Entity;
import cc.allio.turbo.common.db.event.Subscriber;
import cc.allio.turbo.common.db.uno.repository.ITurboCrudRepository;
import cc.allio.turbo.common.db.uno.repository.ITurboCrudRepositoryService;
import cc.allio.uno.core.reflect.ReflectTools;
import cc.allio.uno.data.orm.executor.AggregateCommandExecutor;

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
    private Subscriber<T> proxySubscriber;

    protected TurboCrudRepositoryServiceImpl(AggregateCommandExecutor commandExecutor) {
        this(commandExecutor, null);
    }

    protected TurboCrudRepositoryServiceImpl(AggregateCommandExecutor commandExecutor, Class<T> entityClass) {
        if (entityClass == null) {
            this.repository = new SimpleTurboCurdRepositoryImpl<>(commandExecutor, this::getEntityClass);
        } else {
            this.repository = new SimpleTurboCurdRepositoryImpl<>(commandExecutor, entityClass);
        }
        this.entityClass = entityClass;
    }

    @Override
    public ITurboCrudRepository<T> getRepository() {
        return repository;
    }

    @Override
    public void setProxy(Subscriber<T> subscriber) {
        this.proxySubscriber = subscriber;
    }

    @Override
    public Subscriber<T> getProxy() {
        return proxySubscriber;
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
}
