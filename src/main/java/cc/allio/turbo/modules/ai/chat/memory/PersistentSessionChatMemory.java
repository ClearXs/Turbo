package cc.allio.turbo.modules.ai.chat.memory;

import cc.allio.turbo.modules.ai.entity.AIMessage;
import cc.allio.turbo.modules.ai.enums.MessageStatus;
import cc.allio.turbo.modules.ai.enums.Role;
import cc.allio.turbo.modules.ai.store.ChatMessageStore;
import cc.allio.turbo.modules.ai.store.InMemoryChatMessageStore;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.List;

/**
 * Persistent chat memory
 *
 * @author j.x
 * @since 0.2.0
 */
public class PersistentSessionChatMemory extends AuthenticationSessionChatMemory implements ChatMemoryRepository, ChatMemory {

    protected final ChatMessageStore chatMessageStorage;

    public PersistentSessionChatMemory(Authentication authentication,
                                       String sessionId,
                                       ObjectProvider<ChatMessageStore> chatMessageStorageObjectProvider) {
        this(authentication, sessionId, chatMessageStorageObjectProvider.getIfAvailable(InMemoryChatMessageStore::new));
    }

    public PersistentSessionChatMemory(Authentication authentication,
                                       String sessionId,
                                       ChatMessageStore chatMessageStore) {
        super(authentication, sessionId);
        this.chatMessageStorage = chatMessageStore;
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        saveAll(conversationId, messages);
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        if (AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY.equals(conversationId)) {
            return Collections.emptyList();
        }

        List<AIMessage> aiMessages =
                aroundAuthentication(() ->
                        chatMessageStorage.list(Wrappers.<AIMessage>lambdaQuery().eq(AIMessage::getChatId, conversationId).last("limit " + lastN)));

        return aiMessages.stream()
                .map(aiMessage -> {
                    Role role = aiMessage.getRole();
                    MessageType messageType = role.toMessageType();
                    Message message = null;
                    if (messageType == MessageType.USER) {
                        // TODO consider media.
                        message = new UserMessage(aiMessage.getContent());
                    } else if (messageType == MessageType.ASSISTANT) {
                        // TODO consider tool withCall and properties and media.
                        message = new AssistantMessage(aiMessage.getContent());
                    } else if (messageType == MessageType.SYSTEM) {
                        message = new SystemMessage(aiMessage.getContent());
                    }
                    return message;
                })
                .toList();
    }

    @Override
    public void clear(String conversationId) {
        aroundAuthentication(() ->
                chatMessageStorage.remove(Wrappers.<AIMessage>lambdaQuery().eq(AIMessage::getChatId, Long.valueOf(conversationId))));
    }

    @Override
    public List<String> findConversationIds() {
        return List.of();
    }

    @Override
    public List<Message> findByConversationId(String conversationId) {
        List<AIMessage> aiMessages =
                aroundAuthentication(() ->
                        chatMessageStorage.list(Wrappers.<AIMessage>lambdaQuery().eq(AIMessage::getChatId, conversationId)));

        return aiMessages.stream()
                .map(aiMessage -> {
                    Role role = aiMessage.getRole();
                    MessageType messageType = role.toMessageType();
                    Message message = null;
                    if (messageType == MessageType.USER) {
                        // TODO consider media.
                        message = new UserMessage(aiMessage.getContent());
                    } else if (messageType == MessageType.ASSISTANT) {
                        // TODO consider tool withCall and properties and media.
                        message = new AssistantMessage(aiMessage.getContent());
                    } else if (messageType == MessageType.SYSTEM) {
                        message = new SystemMessage(aiMessage.getContent());
                    }
                    return message;
                })
                .toList();
    }

    @Override
    public void saveAll(String conversationId, List<Message> messages) {
        if (AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY.equals(conversationId)) {
            return;
        }

        // filter user message
        List<AIMessage> aiMessages =
                messages.stream()
                        .filter(message -> !MessageType.USER.equals(message.getMessageType()))
                        .map(message -> {
                            AIMessage aiMessage = new AIMessage();
                            aiMessage.setChatId(Long.valueOf(conversationId));
                            aiMessage.setSessionId(getSessionId());
                            aiMessage.setContent(message.getText());
                            aiMessage.setRole(Role.fromMessageType(message.getMessageType()));
                            aiMessage.setState(MessageStatus.COMPLETE);
                            return aiMessage;
                        })
                        .toList();

        aroundAuthentication(() -> chatMessageStorage.saveBatch(aiMessages));
    }

    @Override
    public void deleteByConversationId(String conversationId) {
        aroundAuthentication(() ->
                chatMessageStorage.remove(Wrappers.<AIMessage>lambdaQuery().eq(AIMessage::getChatId, Long.valueOf(conversationId))));
    }
}
