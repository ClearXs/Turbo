package cc.allio.turbo.modules.developer.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * AttributeType
 *
 * @author jiangwei
 * @date 2024/1/18 17:33
 * @since 0.1.0
 */
@Getter
@AllArgsConstructor
public enum AttributeType {

    TABLE("table", "表"),
    FIELD("field", "字段");

    @JsonValue
    @EnumValue
    private final String value;
    private final String label;
}
