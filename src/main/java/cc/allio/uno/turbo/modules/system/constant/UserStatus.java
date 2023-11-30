package cc.allio.uno.turbo.modules.system.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户状态
 *
 * @author j.x
 * @date 2023/11/1 16:42
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum UserStatus {
    ENABLE("ENABLE", "启用"),
    LOCK("LOCK", "锁定");

    @JsonValue
    @EnumValue
    private final String value;
    private final String label;
}
