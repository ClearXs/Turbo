package cc.allio.turbo.modules.developer.constant;

import cc.allio.uno.data.orm.dsl.type.DSLType;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * attribute字段类型
 *
 * @author j.x
 * @date 2024/1/18 17:33
 * @since 0.1.0
 */
@Getter
@AllArgsConstructor
public enum FieldType {

    // ====================== 数字型 ======================
    BIGINT(DSLType.BIGINT.getName(), DSLType.BIGINT.getName(), DSLType.BIGINT),
    SMALLINT(DSLType.SMALLINT.getName(), DSLType.SMALLINT.getName(), DSLType.SMALLINT),
    INTEGER(DSLType.INTEGER.getName(), DSLType.INTEGER.getName(), DSLType.INTEGER),
    BIT(DSLType.BIT.getName(), DSLType.BIT.getName(), DSLType.BIT),
    TINYINT(DSLType.TINYINT.getName(), DSLType.TINYINT.getName(), DSLType.TINYINT),
    NUMBER(DSLType.NUMBER.getName(), DSLType.NUMBER.getName(), DSLType.NUMBER),
    DOUBLE(DSLType.DOUBLE.getName(), DSLType.DOUBLE.getName(), DSLType.DOUBLE),
    FLOAT(DSLType.FLOAT.getName(), DSLType.FLOAT.getName(), DSLType.FLOAT),
    DECIMAL(DSLType.DECIMAL.getName(), DSLType.DECIMAL.getName(), DSLType.DECIMAL),

    // ====================== 时间型 ======================,
    TIME(DSLType.TIME.getName(), DSLType.TIME.getName(), DSLType.TIME),
    TIMESTAMP(DSLType.TIMESTAMP.getName(), DSLType.TIMESTAMP.getName(), DSLType.TIMESTAMP),
    DATE(DSLType.DATE.getName(), DSLType.DATE.getName(), DSLType.DATE),

    // ====================== 字符型 ======================
    CHAR(DSLType.CHAR.getName(), DSLType.CHAR.getName(), DSLType.CHAR),
    VARCHAR(DSLType.VARCHAR.getName(), DSLType.VARCHAR.getName(), DSLType.VARCHAR),
    NVARCHAR(DSLType.NVARCHAR.getName(), DSLType.NVARCHAR.getName(), DSLType.NVARCHAR),
    LONGVARCHAR(DSLType.LONGVARCHAR.getName(), DSLType.LONGVARCHAR.getName(), DSLType.LONGVARCHAR),
    LONGNVARCHAR(DSLType.LONGNVARCHAR.getName(), DSLType.LONGNVARCHAR.getName(), DSLType.LONGNVARCHAR),
    VARBINARY(DSLType.VARBINARY.getName(), DSLType.VARBINARY.getName(), DSLType.VARBINARY),
    LONGVARBINARY(DSLType.LONGVARBINARY.getName(), DSLType.LONGVARBINARY.getName(), DSLType.LONGVARBINARY),

    // ====================== 高级类型 ======================
    OBJECT(DSLType.OBJECT.getName(), DSLType.OBJECT.getName(), DSLType.OBJECT),
    ARRAY(DSLType.ARRAY.getName(), DSLType.ARRAY.getName(), DSLType.ARRAY);

    @JsonValue
    @EnumValue
    private final String value;
    private final String label;
    private final DSLType dslType;
}
