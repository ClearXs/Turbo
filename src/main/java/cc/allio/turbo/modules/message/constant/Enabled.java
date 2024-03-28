
package cc.allio.turbo.modules.message.constant;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Enabled {

    ON("ON", "启用"),
    OFF("OFF", "关闭");

    @JsonValue
    private final String value;
    private final String label;
}