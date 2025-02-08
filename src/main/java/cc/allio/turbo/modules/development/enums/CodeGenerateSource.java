package cc.allio.turbo.modules.development.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * code generate source
 *
 * @author j.x
 * @date 2024/5/3 16:43
 * @since 0.1.1
 */
@Getter
@AllArgsConstructor
public enum CodeGenerateSource {

    DATATABLE("datatable", "实体表"),
    BO("bo", "BO对象"),
    PAGE("page", "数据页面");

    @JsonValue
    @EnumValue
    private final String value;
    private final String label;
}
