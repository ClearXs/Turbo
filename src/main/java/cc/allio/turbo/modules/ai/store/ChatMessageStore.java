package cc.allio.turbo.modules.ai.store;

import cc.allio.turbo.common.db.uno.repository.ITurboCrudRepositoryService;
import cc.allio.turbo.modules.ai.entity.AIMessage;

public interface ChatMessageStore extends ITurboCrudRepositoryService<AIMessage> {
}
