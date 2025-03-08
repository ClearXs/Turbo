package cc.allio.turbo.common.db.uno.repository.impl;

import cc.allio.turbo.common.db.entity.Entity;
import cc.allio.turbo.common.db.uno.repository.ITurboCrudRepositoryService;
import cc.allio.uno.data.orm.executor.AggregateCommandExecutor;

/**
 * default impl class for {@link ITurboCrudRepositoryService}
 *
 * @author j.x
 * @date 2024/2/29 23:29
 * @since 0.1.1
 */
public class SimpleTurboCrudRepositoryServiceImpl<T extends Entity> extends TurboCrudRepositoryServiceImpl<T> {

    public SimpleTurboCrudRepositoryServiceImpl() {
        this(null, null);
    }

    public SimpleTurboCrudRepositoryServiceImpl(Class<T> entityClass) {
        this(null, entityClass);
    }

    public SimpleTurboCrudRepositoryServiceImpl(AggregateCommandExecutor commandExecutor) {
        this(commandExecutor, null);
    }

    public SimpleTurboCrudRepositoryServiceImpl(AggregateCommandExecutor commandExecutor, Class<T> entityClass) {
        super(commandExecutor, entityClass);
    }
}
