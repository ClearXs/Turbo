package cc.allio.turbo.common.db.uno.repository.impl;

import cc.allio.turbo.common.db.entity.Entity;
import cc.allio.turbo.common.db.event.Subscriber;
import cc.allio.turbo.common.db.uno.repository.ITurboCrudRepository;
import cc.allio.turbo.common.db.uno.repository.ITurboCrudRepositoryService;
import cc.allio.uno.core.util.ReflectTool;
import cc.allio.uno.data.orm.executor.CommandExecutor;

import java.io.Serializable;

public abstract class TurboCrudRepositoryServiceImpl<T extends Entity> implements ITurboCrudRepositoryService<T> {

    private final ITurboCrudRepository<T> repository;
    private final Class<T> entityClass;
    private Subscriber<T> proxySubscriber;

    protected TurboCrudRepositoryServiceImpl(CommandExecutor commandExecutor) {
        this(commandExecutor, null);
    }

    protected TurboCrudRepositoryServiceImpl(CommandExecutor commandExecutor, Class<T> entityClass) {
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
    public <V extends T> V details(Serializable id) {
        return (V) getById(id);
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
            return (Class<T>) ReflectTool.getGenericType(this, TurboCrudRepositoryServiceImpl.class, 0);
        }
        return entityClass;
    }
}
