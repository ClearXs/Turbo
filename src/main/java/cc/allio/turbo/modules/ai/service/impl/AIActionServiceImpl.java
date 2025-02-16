package cc.allio.turbo.modules.ai.service.impl;

import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl;
import cc.allio.turbo.modules.ai.entity.AIAction;
import cc.allio.turbo.modules.ai.service.IAIActionService;
import org.springframework.stereotype.Service;

@Service
public class AIActionServiceImpl extends SimpleTurboCrudRepositoryServiceImpl<AIAction> implements IAIActionService {
}
