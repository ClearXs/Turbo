package cc.allio.turbo.modules.ai.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Method {

    HTTP("http", "http"),
    WS("ws", "websocket");

    @JsonValue
    @EnumValue
    private final String value;
    private final String label;
}
