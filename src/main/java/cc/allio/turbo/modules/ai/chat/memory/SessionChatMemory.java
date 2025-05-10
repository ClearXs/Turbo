package cc.allio.turbo.modules.ai.chat.memory;

import lombok.Getter;
import org.springframework.ai.chat.memory.ChatMemoryRepository;

public abstract class SessionChatMemory implements ChatMemoryRepository {

    @Getter
    private final String sessionId;

    protected SessionChatMemory(String sessionId) {
        this.sessionId = sessionId;
    }
}
