package cc.allio.turbo.modules.message.config;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Protocol {

    WEBSOCKET("WEBSOCKET", "WEBSOCKET"),
    MQTT("MQTT", "MQTT");

    @JsonValue
    private final String value;
    private final String label;
}
