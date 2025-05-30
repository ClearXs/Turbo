package cc.allio.turbo.modules.ai.api.service.impl;

import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl;
import cc.allio.turbo.modules.ai.entity.AIMessage;
import cc.allio.turbo.modules.ai.api.service.IAIMessageService;
import org.springframework.stereotype.Service;

@Service
public class AIMessageServiceImpl extends SimpleTurboCrudRepositoryServiceImpl<AIMessage> implements IAIMessageService {
}
