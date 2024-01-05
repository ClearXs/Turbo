package cc.allio.turbo.modules.system.constant;

import cc.allio.turbo.common.excel.annotaion.EnumExcelValue;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户状态
 *
 * @author j.x
 * @date 2023/11/1 16:42
 * @since 0.1.0
 */
@Getter
@AllArgsConstructor
public enum UserStatus {
    ENABLE("ENABLE", "启用"),
    LOCK("LOCK", "锁定");

    @JsonValue
    @EnumValue
    private final String value;
    @EnumExcelValue
    private final String label;
}
