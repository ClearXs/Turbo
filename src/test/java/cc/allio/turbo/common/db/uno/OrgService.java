package cc.allio.turbo.common.db.uno;

import cc.allio.turbo.common.db.entity.Org;
import cc.allio.turbo.common.db.uno.repository.ITurboCrudTreeRepositoryService;
import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudTreeRepositoryServiceImpl;
import cc.allio.uno.data.orm.executor.AggregateCommandExecutor;
import org.springframework.stereotype.Service;

@Service
public class OrgService extends SimpleTurboCrudTreeRepositoryServiceImpl<Org> implements ITurboCrudTreeRepositoryService<Org> {

    public OrgService(AggregateCommandExecutor commandExecutor, Class<Org> orgServiceClass) {
        super(commandExecutor, orgServiceClass);
    }
}
