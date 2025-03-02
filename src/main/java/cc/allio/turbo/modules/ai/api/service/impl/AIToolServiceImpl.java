package cc.allio.turbo.modules.ai.api.service.impl;

import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl;
import cc.allio.turbo.modules.ai.api.entity.AITool;
import cc.allio.turbo.modules.ai.api.service.IAIToolService;
import org.springframework.stereotype.Service;

@Service
public class AIToolServiceImpl extends SimpleTurboCrudRepositoryServiceImpl<AITool> implements IAIToolService {
}
