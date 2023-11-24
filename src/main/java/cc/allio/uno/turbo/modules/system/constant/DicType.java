package cc.allio.uno.turbo.modules.system.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 字典类型
 *
 * @author j.x
 * @date 2023/11/23 09:06
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum DicType {

    SYSTEM("SYSTEM", "系统类"),
    BIZ("BIZ", "业务类");

    @JsonValue
    @EnumValue
    private final String value;
    private final String label;
}
