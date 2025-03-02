package cc.allio.turbo.modules.ai.chat.memory;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.messages.Message;

import java.util.List;

public class SessionInMemoryChatMemory extends SessionChatMemory implements ChatMemory {

    private final ChatMemory actual;

    public SessionInMemoryChatMemory(String sessionId) {
        super(sessionId);
        this.actual = new InMemoryChatMemory();
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        actual.add(conversationId, messages);
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        return actual.get(conversationId, lastN);
    }

    @Override
    public void clear(String conversationId) {
        actual.clear(conversationId);
    }
}
