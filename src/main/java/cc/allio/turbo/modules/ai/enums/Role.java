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
    ASSISTANT("assistant", "assistant");

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
        }
        return null;
    }
}
