package cc.allio.turbo.modules.development.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据集来源
 *
 * @author j.x
 * @date 2024/1/24 14:26
 * @since 0.1.0
 */
@Getter
@AllArgsConstructor
public enum DatasetSource {

    BO("bo", "业务对象"),
    API("api", "接口"),
    SQL("sql", "sql");

    @JsonValue
    @EnumValue
    private final String value;
    private final String label;
}
