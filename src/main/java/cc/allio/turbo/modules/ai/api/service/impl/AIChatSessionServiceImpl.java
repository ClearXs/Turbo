package cc.allio.turbo.modules.ai.api.service.impl;

import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl;
import cc.allio.turbo.modules.ai.api.entity.AIChatSession;
import cc.allio.turbo.modules.ai.api.service.IAIChatSessionService;
import org.springframework.stereotype.Service;

@Service
public class AIChatSessionServiceImpl extends SimpleTurboCrudRepositoryServiceImpl<AIChatSession> implements IAIChatSessionService {
}
