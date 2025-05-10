package cc.allio.turbo.modules.ai.store;

import cc.allio.turbo.common.db.uno.repository.InMemoryTurboCrudRepositoryService;
import cc.allio.turbo.modules.ai.entity.AIChatSession;

public class InMemoryChatSessionStore extends InMemoryTurboCrudRepositoryService<AIChatSession> implements ChatSessionStore {
}
