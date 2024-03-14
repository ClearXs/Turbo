package cc.allio.turbo.modules.system.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageSource {

    SYSTEM("SYSTEM", "系统");

    @JsonValue
    @EnumValue
    private final String value;
    private final String label;
}
