package cc.allio.turbo.modules.ai.api.service.impl;

import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl;
import cc.allio.turbo.modules.ai.entity.AIAgent;
import cc.allio.turbo.modules.ai.api.service.IAIAgentService;
import org.springframework.stereotype.Service;

@Service
public class AIAgentServiceImpl extends SimpleTurboCrudRepositoryServiceImpl<AIAgent> implements IAIAgentService {

}
