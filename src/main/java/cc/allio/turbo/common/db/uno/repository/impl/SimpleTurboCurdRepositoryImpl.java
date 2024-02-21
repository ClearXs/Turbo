package cc.allio.turbo.common.db.uno.repository.impl;

import cc.allio.turbo.common.db.entity.Entity;
import cc.allio.turbo.common.db.uno.repository.ITurboCrudRepository;
import cc.allio.uno.core.api.Step;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.CommandExecutorFactory;

import java.util.function.Supplier;

/**
 * 基于系统内置{@link CommandExecutor}的CRUD实现
 *
 * @author jiangwei
 * @date 2024/2/4 22:20
 * @since 0.1.0
 */
public class SimpleTurboCurdRepositoryImpl<T extends Entity> extends TurboCrudRepositoryImpl<T> implements ITurboCrudRepository<T> {

    private final CommandExecutor commandExecutor;
    private final Class<T> entityType;
    private final Supplier<Class<T>> entityClassGetter;

    public SimpleTurboCurdRepositoryImpl() {
        this(CommandExecutorFactory.getDSLExecutor());
    }

    public SimpleTurboCurdRepositoryImpl(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
        this.entityType = null;
        this.entityClassGetter = null;
    }

    public SimpleTurboCurdRepositoryImpl(CommandExecutor commandExecutor, Class<T> entityType) {
        this.commandExecutor = commandExecutor;
        this.entityType = entityType;
        this.entityClassGetter = null;
    }

    public SimpleTurboCurdRepositoryImpl(CommandExecutor commandExecutor, Supplier<Class<T>> entityClassGetter) {
        this.commandExecutor = commandExecutor;
        this.entityType = null;
        this.entityClassGetter = entityClassGetter;
    }

    @Override
    public CommandExecutor getExecutor() {
        if (commandExecutor == null) {
            return super.getExecutor();
        }
        return commandExecutor;
    }

    @Override
    public Class<T> getEntityType() {
        return Step.<Class<T>>start()
                .then(() -> entityType)
                .then(entityClassGetter)
                .then(super::getEntityType)
                .stop();
    }
}
