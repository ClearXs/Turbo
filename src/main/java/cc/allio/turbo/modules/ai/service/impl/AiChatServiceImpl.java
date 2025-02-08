package cc.allio.turbo.modules.ai.service.impl;

import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl;
import cc.allio.turbo.modules.ai.entity.AiChat;
import cc.allio.turbo.modules.ai.service.IAiChatService;
import org.springframework.stereotype.Service;

@Service
public class AiChatServiceImpl extends SimpleTurboCrudRepositoryServiceImpl<AiChat> implements IAiChatService {
}
