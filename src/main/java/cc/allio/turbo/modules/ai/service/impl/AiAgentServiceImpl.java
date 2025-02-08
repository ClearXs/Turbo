package cc.allio.turbo.modules.ai.service.impl;

import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl;
import cc.allio.turbo.modules.ai.entity.AiAgent;
import cc.allio.turbo.modules.ai.service.IAiAgentService;
import org.springframework.stereotype.Service;

@Service
public class AiAgentServiceImpl extends SimpleTurboCrudRepositoryServiceImpl<AiAgent> implements IAiAgentService {
    
}
