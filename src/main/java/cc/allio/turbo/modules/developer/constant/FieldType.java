package cc.allio.turbo.modules.developer.constant;

import cc.allio.uno.data.orm.dsl.type.DSLType;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * attribute字段类型
 *
 * @author jiangwei
 * @date 2024/1/18 17:33
 * @since 0.1.0
 */
@Getter
@AllArgsConstructor
public enum FieldType {

    // ====================== 数字型 ======================
    BIGINT(DSLType.BIGINT.getName(), DSLType.BIGINT.getName()),
    SMALLINT(DSLType.SMALLINT.getName(), DSLType.SMALLINT.getName()),
    INTEGER(DSLType.INTEGER.getName(), DSLType.INTEGER.getName()),
    BIT(DSLType.BIT.getName(), DSLType.BIT.getName()),
    TINYINT(DSLType.TINYINT.getName(), DSLType.TINYINT.getName()),
    NUMBER(DSLType.NUMBER.getName(), DSLType.NUMBER.getName()),
    DOUBLE(DSLType.DOUBLE.getName(), DSLType.DOUBLE.getName()),
    FLOAT(DSLType.FLOAT.getName(), DSLType.FLOAT.getName()),
    DECIMAL(DSLType.DECIMAL.getName(), DSLType.DECIMAL.getName()),

    // ====================== 时间型 ======================,
    TIME(DSLType.TIME.getName(), DSLType.TIME.getName()),
    TIMESTAMP(DSLType.TIMESTAMP.getName(), DSLType.TIMESTAMP.getName()),
    DATE(DSLType.DATE.getName(), DSLType.DATE.getName()),

    // ====================== 字符型 ======================
    CHAR(DSLType.CHAR.getName(), DSLType.CHAR.getName()),
    VARCHAR(DSLType.VARCHAR.getName(), DSLType.VARCHAR.getName()),
    NVARCHAR(DSLType.NVARCHAR.getName(), DSLType.NVARCHAR.getName()),
    LONGVARCHAR(DSLType.LONGVARCHAR.getName(), DSLType.LONGVARCHAR.getName()),
    LONGNVARCHAR(DSLType.LONGNVARCHAR.getName(), DSLType.LONGNVARCHAR.getName()),
    VARBINARY(DSLType.VARBINARY.getName(), DSLType.VARBINARY.getName()),
    LONGVARBINARY(DSLType.LONGVARBINARY.getName(), DSLType.LONGVARBINARY.getName()),

    // ====================== 高级类型 ======================
    OBJECT(DSLType.OBJECT.getName(), DSLType.OBJECT.getName()),
    ARRAY(DSLType.ARRAY.getName(), DSLType.ARRAY.getName());

    @JsonValue
    @EnumValue
    private final String value;
    private final String label;
}
