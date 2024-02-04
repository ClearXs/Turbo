package cc.allio.turbo.common.db.uno.repository.impl;

import cc.allio.turbo.common.db.entity.Entity;
import cc.allio.turbo.common.db.uno.repository.ITurboCrudRepository;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.CommandExecutorFactory;

import java.io.Serializable;

/**
 * 基于系统内置的CRUD实现
 *
 * @author jiangwei
 * @date 2024/2/4 22:20
 * @since 0.1.0
 */
public class SimpleTurboCurdRepositoryImpl<T extends Entity, ID extends Serializable> extends TurboCrudRepositoryImpl<T, ID> implements ITurboCrudRepository<T, ID> {

    private final CommandExecutor commandExecutor;

    public SimpleTurboCurdRepositoryImpl() {
        this(CommandExecutorFactory.getDSLExecutor());
    }

    public SimpleTurboCurdRepositoryImpl(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    @Override
    public CommandExecutor getExecutor() {
        return commandExecutor;
    }
}
