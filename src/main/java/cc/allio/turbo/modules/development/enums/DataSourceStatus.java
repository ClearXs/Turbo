package cc.allio.turbo.modules.development.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DataSourceStatus {

    LIVED("lived", "存活"),
    DEAD("dead", "断开"),
    BLOCKED("blocked", "阻塞");

    @JsonValue
    @EnumValue
    private final String value;
    private final String label;
}
