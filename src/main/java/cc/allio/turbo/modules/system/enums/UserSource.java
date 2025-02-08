package cc.allio.turbo.modules.system.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserSource {

    SELF_BUILT("SELF-BUILT", "自建"),
    THIRD("THIRD", "第三方");

    @JsonValue
    @EnumValue
    private final String value;
    private final String label;
}
