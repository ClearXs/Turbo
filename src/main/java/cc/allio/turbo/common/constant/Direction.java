package cc.allio.turbo.common.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Direction {

    ASC("ASC", "升序"),
    DESC("DESC", "降序");

    @JsonValue
    @EnumValue
    private final String value;
    private final String label;
}
