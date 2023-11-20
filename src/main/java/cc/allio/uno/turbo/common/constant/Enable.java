package cc.allio.uno.turbo.common.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用启、停常量
 *
 * @author j.x
 * @date 2023/11/16 17:50
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum Enable {

    /**
     * 启用
     */
    ENABLE("ENABLE", "启用"),

    /**
     * 停用
     */
    DISABLE("DISABLE", "停用");

    @JsonValue
    @EnumValue
    private final String value;
    private final String label;
}
