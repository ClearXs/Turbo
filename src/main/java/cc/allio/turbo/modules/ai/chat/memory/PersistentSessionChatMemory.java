package cc.allio.turbo.modules.ai.chat.memory;

import cc.allio.turbo.modules.ai.api.entity.AIMessage;
import cc.allio.turbo.modules.ai.enums.Role;
import cc.allio.uno.data.orm.executor.AggregateCommandExecutor;
import cc.allio.uno.data.orm.executor.CommandExecutorFactory;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.*;

import java.util.Collections;
import java.util.List;

/**
 * Persistent chat memory
 *
 * @author j.x
 * @since 0.2.0
 */
public class PersistentSessionChatMemory extends SessionChatMemory implements ChatMemory {

    public PersistentSessionChatMemory(String sessionId) {
        super(sessionId);
    }

    @Override
    public void add(String conversationId, List<Message> messages) {

        if (AbstractChatMemoryAdvisor.DEFAULT_CHAT_MEMORY_CONVERSATION_ID.equals(conversationId)) {
            return;
        }

        List<AIMessage> aiMessages =
                messages.stream()
                        .map(message -> {
                            AIMessage aiMessage = new AIMessage();
                            aiMessage.setChatId(Long.valueOf(conversationId));
                            aiMessage.setSessionId(Long.valueOf(getSessionId()));
                            aiMessage.setContent(message.getText());
                            aiMessage.setRole(Role.fromMessageType(message.getMessageType()));
                            return aiMessage;
                        })
                        .toList();

        AggregateCommandExecutor commandExecutor = getCommandExecutor();
        if (commandExecutor != null) {
            commandExecutor.batchInsertPojos(aiMessages);
        }
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {

        if (AbstractChatMemoryAdvisor.DEFAULT_CHAT_MEMORY_CONVERSATION_ID.equals(conversationId)) {
            return Collections.emptyList();
        }

        AggregateCommandExecutor commandExecutor = getCommandExecutor();
        if (commandExecutor != null) {
            List<AIMessage> aiMessages =
                    commandExecutor.queryList(
                            AIMessage.class,
                            q -> q.selectAll()
                                    .eq(AIMessage::getChatId, Long.valueOf(conversationId))
                                    .limit(1L, (long) lastN));

            return aiMessages.stream()
                    .map(aiMessage -> {
                        Role role = aiMessage.getRole();
                        MessageType messageType = role.toMessageType();
                        Message message = null;
                        if (messageType == MessageType.USER) {
                            // TODO consider media.
                            message = new UserMessage(aiMessage.getContent());
                        } else if (messageType == MessageType.ASSISTANT) {
                            // TODO consider tool call and properties and media.
                            message = new AssistantMessage(aiMessage.getContent());
                        } else if (messageType == MessageType.SYSTEM) {
                            message = new SystemMessage(aiMessage.getContent());
                        }
                        return message;
                    })
                    .toList();
        }
        return Collections.emptyList();
    }

    @Override
    public void clear(String conversationId) {

        if (AbstractChatMemoryAdvisor.DEFAULT_CHAT_MEMORY_CONVERSATION_ID.equals(conversationId)) {
            return;
        }

        AggregateCommandExecutor commandExecutor = getCommandExecutor();
        if (commandExecutor != null) {
            commandExecutor.delete(AIMessage.class, q -> q.eq(AIMessage::getChatId, Long.valueOf(conversationId)));
        }
    }

    AggregateCommandExecutor getCommandExecutor() {
        return CommandExecutorFactory.getDSLExecutor();
    }
}
