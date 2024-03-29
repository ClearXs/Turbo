package cc.allio.turbo.common.db.uno.repository.impl;

import cc.allio.turbo.common.db.entity.Entity;
import cc.allio.turbo.common.db.uno.repository.ITurboCrudRepositoryService;
import cc.allio.uno.data.orm.executor.AggregateCommandExecutor;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.CommandExecutorFactory;

/**
 * default impl class for {@link ITurboCrudRepositoryService}
 *
 * @author j.x
 * @date 2024/2/29 23:29
 * @since 0.1.1
 */
public class SimpleTurboCrudRepositoryServiceImpl<T extends Entity> extends TurboCrudRepositoryServiceImpl<T> {

    public SimpleTurboCrudRepositoryServiceImpl() {
        this(CommandExecutorFactory.getDSLExecutor(), null);
    }

    public SimpleTurboCrudRepositoryServiceImpl(Class<T> entityClass) {
        this(CommandExecutorFactory.getDSLExecutor(), entityClass);
    }

    public SimpleTurboCrudRepositoryServiceImpl(AggregateCommandExecutor commandExecutor) {
        this(commandExecutor, null);
    }

    public SimpleTurboCrudRepositoryServiceImpl(AggregateCommandExecutor commandExecutor, Class<T> entityClass) {
        super(commandExecutor, entityClass);
    }
}
