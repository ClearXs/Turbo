package cc.allio.turbo.common.db.uno.repository.impl;

import cc.allio.turbo.common.db.entity.TreeNodeEntity;
import cc.allio.turbo.common.db.uno.repository.ITurboCrudTreeRepositoryService;
import cc.allio.uno.data.orm.executor.CommandExecutor;

public abstract class TurboCrudTreeRepositoryServiceImpl<T extends TreeNodeEntity> extends TurboCrudRepositoryServiceImpl<T> implements ITurboCrudTreeRepositoryService<T> {

    protected TurboCrudTreeRepositoryServiceImpl(CommandExecutor commandExecutor) {
        super(commandExecutor);
    }

}
