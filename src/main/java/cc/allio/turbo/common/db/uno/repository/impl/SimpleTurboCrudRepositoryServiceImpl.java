package cc.allio.turbo.common.db.uno.repository.impl;

import cc.allio.turbo.common.db.entity.Entity;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.CommandExecutorFactory;

public class SimpleTurboCrudRepositoryServiceImpl<T extends Entity> extends TurboCrudRepositoryServiceImpl<T> {

    public SimpleTurboCrudRepositoryServiceImpl() {
        this(CommandExecutorFactory.getDSLExecutor(), null);
    }

    public SimpleTurboCrudRepositoryServiceImpl(CommandExecutor commandExecutor) {
        this(commandExecutor, null);
    }

    public SimpleTurboCrudRepositoryServiceImpl(CommandExecutor commandExecutor, Class<T> entityClass) {
        super(commandExecutor, entityClass);
    }


}
