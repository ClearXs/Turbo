package cc.allio.turbo.modules.ai.chat.message;

import cc.allio.turbo.modules.ai.enums.Role;
import com.google.common.collect.Maps;
import jakarta.validation.constraints.NotNull;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.metadata.ChatGenerationMetadata;
import org.springframework.ai.chat.model.Generation;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * withCall chat message.
 *
 * @author j.x
 * @since 0.2.0
 */
public interface AdvancedMessage extends org.springframework.ai.chat.messages.Message {

    String UNFINISH = "";
    String FINISH_STOP = "stop";
    String FINISH_LENGTH = "length";
    String FINISH_CONTENT_FILTER = "content_filter";
    String FINISH_ERROR = "error";

    Long id();

    /**
     * message content
     */
    String content();

    /**
     * get finish message
     */
    String finish();

    /**
     * create time
     */
    long createAt();

    /**
     * role of the message
     */
    Role role();

    /**
     * get chat id
     */
    String getConversationId();

    /**
     * get session id
     */
    String getSessionId();

    @Override
    default String getText() {
        return content();
    }

    @Override
    Map<String, Object> getMetadata();

    @Override
    default MessageType getMessageType() {
        return Optional.ofNullable(role()).map(Role::toMessageType).orElse(null);
    }

    /**
     * create {@link AdvancedMessage} from {@link Generation}
     *
     * @param generation     the {@link Generation} instance
     * @param conversationId
     * @param sessionId
     * @return
     */
    static AdvancedMessage fromGeneration(@NotNull Generation generation, String conversationId, String sessionId) {
        AssistantMessage output = generation.getOutput();
        ChatGenerationMetadata metadata = generation.getMetadata();
        String finishReason = metadata.getFinishReason();
        MessageImpl message = new MessageImpl();
        message.setFinish(finishReason);

        MessageType messageType = output.getMessageType();
        message.setRole(Role.fromMessageType(messageType));
        message.setCreateAt(message.getCreateAt());
        message.setContent(output.getText());

        // set session id and conversation id
        message.setSessionId(sessionId);
        message.setConversationId(conversationId);

        // set metadata
        Map<String, Object> metadataMap = Maps.newHashMap();
        Set<Map.Entry<String, Object>> entries = metadata.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            metadataMap.put(entry.getKey(), entry.getValue());
        }
        message.setMetadata(metadataMap);

        return message;
    }
}
