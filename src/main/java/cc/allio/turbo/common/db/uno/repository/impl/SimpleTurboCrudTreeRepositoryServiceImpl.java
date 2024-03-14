package cc.allio.turbo.common.db.uno.repository.impl;

import cc.allio.turbo.common.db.entity.TreeNodeEntity;
import cc.allio.turbo.common.db.uno.repository.ITurboCrudTreeRepositoryService;
import cc.allio.uno.data.orm.executor.CommandExecutor;

/**
 * default impl class for {@link ITurboCrudTreeRepositoryService}
 *
 * @author j.x
 * @date 2024/2/29 23:30
 * @since 0.1.1
 */
public class SimpleTurboCrudTreeRepositoryServiceImpl<T extends TreeNodeEntity> extends SimpleTurboCrudRepositoryServiceImpl<T> implements ITurboCrudTreeRepositoryService<T> {

    public SimpleTurboCrudTreeRepositoryServiceImpl(CommandExecutor commandExecutor) {
        super(commandExecutor);
    }

    public SimpleTurboCrudTreeRepositoryServiceImpl(CommandExecutor commandExecutor, Class<T> entityClass) {
        super(commandExecutor, entityClass);
    }
}
