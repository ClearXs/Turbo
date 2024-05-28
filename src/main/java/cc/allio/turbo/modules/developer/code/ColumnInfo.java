package cc.allio.turbo.modules.developer.code;

import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.type.DSLType;
import cc.allio.uno.data.orm.dsl.type.DataType;
import cc.allio.uno.data.orm.dsl.type.JavaType;
import cc.allio.uno.data.orm.dsl.type.TypeRegistry;
import lombok.Data;

import java.util.Optional;

/**
 * brief description {@link ColumnDef}
 *
 * @author j.x
 * @date 2024/5/4 14:42
 * @since 0.1.1
 */
@Data
public class ColumnInfo {

    /**
     * the column name
     */
    public final DSLName name;

    /**
     * the column java type
     */
    public final Class<?> dataType;

    /**
     * the column comment
     */
    private final String comment;

    public ColumnInfo(ColumnDef columnDef) {
        this.name = columnDef.getDslName();
        this.comment = columnDef.getComment();
        Class<?> dataType = Optional.ofNullable(columnDef.getDataType())
                .map(DataType::getDslType)
                .map(DSLType::getJdbcType)
                .map(jdbcType -> TypeRegistry.getInstance().findJavaType(jdbcType))
                .map(JavaType::getJavaType)
                .orElse(null);
        if (dataType == null) {
            dataType = Object.class;
        }
        this.dataType = dataType;
    }
}
