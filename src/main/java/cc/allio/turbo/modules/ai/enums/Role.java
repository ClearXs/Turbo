package cc.allio.turbo.modules.ai.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.ai.chat.messages.MessageType;

@Getter
@AllArgsConstructor
public enum Role {

    USER("user", "user"),
    SYSTEM("system", "system"),
    ASSISTANT("assistant", "assistant"),
    TOOL("tool", "tool");

    @JsonValue
    @EnumValue
    private final String value;
    private final String label;

    public static Role fromMessageType(MessageType messageType) {
        if (MessageType.ASSISTANT == messageType) {
            return ASSISTANT;
        } else if (MessageType.SYSTEM == messageType) {
            return SYSTEM;
        } else if (MessageType.USER == messageType) {
            return USER;
        } else if (MessageType.TOOL == messageType) {
            return TOOL;
        }
        return null;
    }

    public MessageType toMessageType() {
        if (this == USER) {
            return MessageType.USER;
        } else if (this == SYSTEM) {
            return MessageType.SYSTEM;
        } else if (this == ASSISTANT) {
            return MessageType.ASSISTANT;
        } else if (this == TOOL) {
            return MessageType.TOOL;
        }
        return null;
    }
}
