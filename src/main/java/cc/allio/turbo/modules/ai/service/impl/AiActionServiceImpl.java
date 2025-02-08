package cc.allio.turbo.modules.ai.service.impl;

import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl;
import cc.allio.turbo.modules.ai.entity.AiAction;
import cc.allio.turbo.modules.ai.service.IAiActionService;
import org.springframework.stereotype.Service;

@Service
public class AiActionServiceImpl extends SimpleTurboCrudRepositoryServiceImpl<AiAction> implements IAiActionService {
}
