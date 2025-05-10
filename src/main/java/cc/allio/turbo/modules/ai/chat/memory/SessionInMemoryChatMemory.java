package cc.allio.turbo.modules.ai.chat.memory;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;

import java.util.List;

public class SessionInMemoryChatMemory extends SessionChatMemory implements ChatMemoryRepository, ChatMemory {

    private final ChatMemoryRepository actual;

    public SessionInMemoryChatMemory(String sessionId) {
        super(sessionId);
        this.actual = new InMemoryChatMemoryRepository();
    }

    @Override
    public List<String> findConversationIds() {
        return actual.findConversationIds();
    }

    @Override
    public List<Message> findByConversationId(String conversationId) {
        return actual.findByConversationId(conversationId);
    }

    @Override
    public void saveAll(String conversationId, List<Message> messages) {
        actual.saveAll(conversationId, messages);
    }

    @Override
    public void deleteByConversationId(String conversationId) {
        actual.deleteByConversationId(conversationId);
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        actual.saveAll(conversationId, messages);
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        return actual.findByConversationId(conversationId);
    }

    @Override
    public void clear(String conversationId) {
        actual.deleteByConversationId(conversationId);
    }
}
