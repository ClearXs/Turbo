package cc.allio.turbo.modules.message.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SendStatus {

    SUCCESS("Success", "成功"),
    FAILURE("Failure", "失败");

    @JsonValue
    @EnumValue
    private final String value;
    private final String label;

    public static SendStatus binaryCreate(boolean send) {
        if (send) {
            return SUCCESS;
        } else {
            return FAILURE;
        }
    }
}
