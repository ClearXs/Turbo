package cc.allio.turbo.modules.message.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {

    REMIND("Remind", "提醒"),
    NOTICE("Notice", "通知"),
    ALERT("Alert", "告警");

    @JsonValue
    @EnumValue
    private final String value;
    private final String label;
}
