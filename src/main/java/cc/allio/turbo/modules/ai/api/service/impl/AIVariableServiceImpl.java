package cc.allio.turbo.modules.ai.api.service.impl;

import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl;
import cc.allio.turbo.modules.ai.entity.AIVariable;
import cc.allio.turbo.modules.ai.api.service.IAIVariableService;
import org.springframework.stereotype.Service;

@Service
public class AIVariableServiceImpl extends SimpleTurboCrudRepositoryServiceImpl<AIVariable> implements IAIVariableService {
}
