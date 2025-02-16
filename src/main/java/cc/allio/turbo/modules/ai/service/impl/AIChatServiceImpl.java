package cc.allio.turbo.modules.ai.service.impl;

import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl;
import cc.allio.turbo.modules.ai.entity.AIChat;
import cc.allio.turbo.modules.ai.service.IAIChatService;
import org.springframework.stereotype.Service;

@Service
public class AIChatServiceImpl extends SimpleTurboCrudRepositoryServiceImpl<AIChat> implements IAIChatService {
}
