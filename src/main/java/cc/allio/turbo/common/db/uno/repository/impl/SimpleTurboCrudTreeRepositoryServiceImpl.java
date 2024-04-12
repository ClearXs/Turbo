package cc.allio.turbo.common.db.uno.repository.impl;

import cc.allio.turbo.common.db.entity.TreeNodeEntity;
import cc.allio.turbo.common.db.uno.repository.ITurboCrudTreeRepositoryService;
import cc.allio.uno.data.orm.executor.AggregateCommandExecutor;
import cc.allio.uno.data.orm.executor.CommandExecutor;

/**
 * default impl class for {@link ITurboCrudTreeRepositoryService}
 *
 * @author j.x
 * @date 2024/2/29 23:30
 * @since 0.1.1
 */
public class SimpleTurboCrudTreeRepositoryServiceImpl<T extends TreeNodeEntity> extends SimpleTurboCrudRepositoryServiceImpl<T> implements ITurboCrudTreeRepositoryService<T> {

    public SimpleTurboCrudTreeRepositoryServiceImpl() {
        this(null, null);
    }

    public SimpleTurboCrudTreeRepositoryServiceImpl(Class<T> entityClass) {
        this(null, entityClass);
    }

    public SimpleTurboCrudTreeRepositoryServiceImpl(AggregateCommandExecutor commandExecutor) {
        this(commandExecutor, null);
    }

    public SimpleTurboCrudTreeRepositoryServiceImpl(AggregateCommandExecutor commandExecutor, Class<T> entityClass) {
        super(commandExecutor, entityClass);
    }
}
