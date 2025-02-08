package cc.allio.turbo.modules.ai.service.impl;

import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl;
import cc.allio.turbo.modules.ai.entity.AiMessage;
import cc.allio.turbo.modules.ai.service.IAiMessageService;
import org.springframework.stereotype.Service;

@Service
public class AiMessageServiceImpl extends SimpleTurboCrudRepositoryServiceImpl<AiMessage> implements IAiMessageService {
}
