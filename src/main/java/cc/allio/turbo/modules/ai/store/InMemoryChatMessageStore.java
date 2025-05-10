package cc.allio.turbo.modules.ai.store;

import cc.allio.turbo.common.db.uno.repository.InMemoryTurboCrudRepositoryService;
import cc.allio.turbo.modules.ai.entity.AIMessage;

public class InMemoryChatMessageStore extends InMemoryTurboCrudRepositoryService<AIMessage> implements ChatMessageStore {
}
