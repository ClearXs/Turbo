package cc.allio.turbo.modules.message.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {

    UNREAD("UNREAD", "未读"),
    READ("READ", "已读");

    @JsonValue
    @EnumValue
    private final String value;
    private final String label;
}