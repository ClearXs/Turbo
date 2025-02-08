package cc.allio.turbo.modules.ai.service.impl;

import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl;
import cc.allio.turbo.modules.ai.entity.AiVariable;
import cc.allio.turbo.modules.ai.service.IAiVariableService;
import org.springframework.stereotype.Service;

@Service
public class AiVariableServiceImpl extends SimpleTurboCrudRepositoryServiceImpl<AiVariable> implements IAiVariableService {
}
