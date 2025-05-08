package cc.allio.turbo.modules.ai.agent.runtime;

import cc.allio.turbo.modules.ai.chat.memory.PersistentSessionChatMemory;
import cc.allio.turbo.modules.ai.entity.AIMessage;
import cc.allio.turbo.modules.ai.store.ChatMessageStore;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.core.Authentication;

import java.util.List;

public class EnvironmentPersistentSessionChatMemory extends PersistentSessionChatMemory {

    private final Environment environment;

    public EnvironmentPersistentSessionChatMemory(Authentication authentication,
                                                  String sessionId,
                                                  ObjectProvider<ChatMessageStore> chatMessageStorageObjectProvider,
                                                  Environment environment) {
        super(authentication, sessionId, chatMessageStorageObjectProvider);
        this.environment = environment;
    }

    public EnvironmentPersistentSessionChatMemory(Authentication authentication,
                                                  String sessionId,
                                                  ChatMessageStore chatMessageStore,
                                                  Environment environment) {
        super(authentication, sessionId, chatMessageStore);
        this.environment = environment;
    }

    @Override
    public void add(String conversationId, List<Message> messages) {

        // filter user message
        Message assistant =
                messages.stream()
                .filter(message -> MessageType.ASSISTANT.equals(message.getMessageType()))
                .findFirst()
                .orElse(null);

        AIMessage assistantMessage = environment.getAssistantMessage();
        if (assistant != null && assistantMessage != null) {
            assistantMessage.setContent(assistant.getText());
            aroundAuthentication(() -> chatMessageStorage.updateById(assistantMessage));
        }
    }
}
