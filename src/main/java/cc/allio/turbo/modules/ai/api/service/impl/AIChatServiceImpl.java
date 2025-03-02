package cc.allio.turbo.modules.ai.api.service.impl;

import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl;
import cc.allio.turbo.modules.ai.api.entity.AIChat;
import cc.allio.turbo.modules.ai.api.service.IAIChatService;
import org.springframework.stereotype.Service;

@Service
public class AIChatServiceImpl extends SimpleTurboCrudRepositoryServiceImpl<AIChat> implements IAIChatService {
}
