package cc.allio.turbo.common.db.uno.repository.impl;

import cc.allio.turbo.common.db.entity.Entity;
import cc.allio.turbo.common.db.uno.repository.ITurboCrudRepository;
import cc.allio.turbo.common.db.uno.repository.ITurboRepositoryService;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import com.baomidou.mybatisplus.core.conditions.Wrapper;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class TurboRepositoryServiceImpl<T extends Entity, ID extends Serializable> implements ITurboRepositoryService<T, ID> {

    private final ITurboCrudRepository<T, ID> repository;

    public TurboRepositoryServiceImpl(CommandExecutor commandExecutor) {
        this.repository = new SimpleTurboCurdRepositoryImpl<>(commandExecutor);
    }

    @Override
    public ITurboCrudRepository<T, ID> getRepository() {
        return repository;
    }
}
