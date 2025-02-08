package cc.allio.turbo.modules.ai.service.impl;

import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl;
import cc.allio.turbo.modules.ai.entity.AiModel;
import cc.allio.turbo.modules.ai.service.IAiModelService;
import org.springframework.stereotype.Service;

@Service
public class AiModelServiceImpl extends SimpleTurboCrudRepositoryServiceImpl<AiModel> implements IAiModelService {
}
