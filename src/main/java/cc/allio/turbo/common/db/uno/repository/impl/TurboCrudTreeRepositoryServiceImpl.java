package cc.allio.turbo.common.db.uno.repository.impl;

import cc.allio.turbo.common.db.entity.TreeNodeEntity;
import cc.allio.turbo.common.db.uno.repository.ITurboCrudTreeRepositoryService;
import cc.allio.uno.data.orm.executor.CommandExecutor;

/**
 * base on abstract class {@link ITurboCrudTreeRepositoryService}
 *
 * @author jiangwei
 * @date 2024/2/29 23:30
 * @since 0.1.1
 */
public abstract class TurboCrudTreeRepositoryServiceImpl<T extends TreeNodeEntity> extends TurboCrudRepositoryServiceImpl<T> implements ITurboCrudTreeRepositoryService<T> {

    protected TurboCrudTreeRepositoryServiceImpl(CommandExecutor commandExecutor) {
        super(commandExecutor);
    }
}
