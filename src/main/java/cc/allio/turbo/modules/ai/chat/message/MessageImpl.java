package cc.allio.turbo.modules.ai.chat.message;

import cc.allio.turbo.modules.ai.enums.Role;
import lombok.Data;

import java.util.Map;

/**
 * withCall chat message.
 */
@Data
public class MessageImpl implements AdvancedMessage {

    private Long id;
    private String model;
    private Role role;
    private String content;
    private String finish;
    private long createAt;
    private String conversationId;
    private String sessionId;
    private Map<String, Object> metadata;

    @Override
    public Long id() {
        return id;
    }

    @Override
    public String content() {
        return content;
    }

    @Override
    public String finish() {
        return finish;
    }

    @Override
    public long createAt() {
        return createAt;
    }

    @Override
    public Role role() {
        return role;
    }

    @Override
    public String getConversationId() {
        return conversationId;
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

    @Override
    public Map<String, Object> getMetadata() {
        return metadata;
    }
}
