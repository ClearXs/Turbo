package cc.allio.turbo.common.db.uno.repository.impl;

import cc.allio.turbo.common.db.entity.Entity;
import cc.allio.turbo.common.db.uno.repository.ITurboCrudRepository;
import cc.allio.uno.core.api.Step;
import cc.allio.uno.data.orm.executor.AggregateCommandExecutor;
import cc.allio.uno.data.orm.executor.CommandExecutor;

import java.util.function.Supplier;

/**
 * 基于系统内置{@link CommandExecutor}的CRUD实现
 *
 * @author j.x
 * @date 2024/2/4 22:20
 * @since 0.1.0
 */
public class SimpleTurboCurdRepositoryImpl<T extends Entity> extends TurboCrudRepositoryImpl<T> implements ITurboCrudRepository<T> {

    private final AggregateCommandExecutor commandExecutor;
    private final Supplier<Class<T>> entityClassSupplier;
    private final Supplier<AggregateCommandExecutor> commandExecutorSupplier;
    private Class<T> entityType;

    public SimpleTurboCurdRepositoryImpl(AggregateCommandExecutor commandExecutor, Class<T> entityType) {
        this.commandExecutor = commandExecutor;
        this.entityType = entityType;
        this.entityClassSupplier = null;
        this.commandExecutorSupplier = null;
    }

    public SimpleTurboCurdRepositoryImpl(Supplier<AggregateCommandExecutor> commandExecutorSupplier, Supplier<Class<T>> entityClassSupplier) {
        this.entityType = null;
        this.commandExecutor = null;
        this.commandExecutorSupplier = commandExecutorSupplier;
        this.entityClassSupplier = entityClassSupplier;
    }

    @Override
    public AggregateCommandExecutor getExecutor() {
        return Step.<AggregateCommandExecutor>start()
                .then(() -> commandExecutor)
                .then(commandExecutorSupplier)
                .then(super::getExecutor)
                .end();
    }

    @Override
    public Class<T> getEntityType() {
        return Step.<Class<T>>start()
                .then(() -> entityType)
                .then(entityClassSupplier)
                .then(super::getEntityType)
                .end();
    }

    /**
     * @param entityType entityType
     */
    @Override
    public void setEntityType(Class<T> entityType) {
        this.entityType = entityType;
    }
}
