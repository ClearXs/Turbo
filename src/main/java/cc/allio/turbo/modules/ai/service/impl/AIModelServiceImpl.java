package cc.allio.turbo.modules.ai.service.impl;

import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl;
import cc.allio.turbo.modules.ai.entity.AIModel;
import cc.allio.turbo.modules.ai.service.IAIModelService;
import org.springframework.stereotype.Service;

@Service
public class AIModelServiceImpl extends SimpleTurboCrudRepositoryServiceImpl<AIModel> implements IAIModelService {
}
