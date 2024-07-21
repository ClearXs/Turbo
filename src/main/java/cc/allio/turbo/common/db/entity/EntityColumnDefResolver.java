package cc.allio.turbo.common.db.entity;

import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.dialect.TypeTranslator;
import cc.allio.uno.data.orm.dsl.dialect.TypeTranslatorHolder;
import cc.allio.uno.data.orm.dsl.helper.ColumnDefResolver;
import cc.allio.uno.data.orm.dsl.type.DSLType;
import cc.allio.uno.data.orm.dsl.type.DataType;
import cc.allio.uno.data.orm.dsl.type.JdbcType;
import cc.allio.uno.data.orm.dsl.type.TypeRegistry;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.google.common.collect.Lists;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * mybatis-plus的{@link TableField}解析器
 *
 * @author j.x
 * @date 2024/2/7 00:01
 * @since 0.1.0
 */
public class EntityColumnDefResolver implements ColumnDefResolver {

    @Override
    public ColumnDef resolve(Field field) {
        ColumnDef.ColumnDefBuilder builder = ColumnDef.builder();
        TableId tableId = AnnotationUtils.findAnnotation(field, TableId.class);
        if (tableId != null) {
            builder.isPk(true);
        }
        TableField tableField = AnnotationUtils.findAnnotation(field, TableField.class);
        JdbcType jdbcType = null;
        // resolve jdbc type by @TableField annotation
        // or directly guess jdbc type
        if (tableField != null) {
            builder.dslName(DSLName.of(tableField.value(), DSLName.UNDERLINE_FEATURE));
            org.apache.ibatis.type.JdbcType mybatisJdbcType = tableField.jdbcType();
            if (mybatisJdbcType != org.apache.ibatis.type.JdbcType.UNDEFINED) {
                jdbcType = TypeRegistry.getInstance().getJdbcType(mybatisJdbcType.TYPE_CODE);
            } else {
                Class<?> filedJavaType = field.getType();
                Collection<JdbcType> jdbcTypes = TypeRegistry.getInstance().guessJdbcType(filedJavaType);
                jdbcType = Lists.newArrayList(jdbcTypes).getFirst();
            }
        } else {
            builder.dslName(DSLName.of(field.getName(), DSLName.UNDERLINE_FEATURE));
            Collection<JdbcType> jdbcTypes = TypeRegistry.getInstance().guessJdbcType(field.getType());
            if (CollectionUtils.isNotEmpty(jdbcTypes)) {
                jdbcType = Lists.newArrayList(jdbcTypes).getFirst();
            }
        }
        if (jdbcType != null) {
            DSLType guessSQLType = DSLType.getByJdbcCode(jdbcType.getJdbcCode());
            if (guessSQLType != null) {
                TypeTranslator translator = TypeTranslatorHolder.getTypeTranslator();
                DSLType sqlType = translator.translate(guessSQLType);
                DataType type = DataType.create(sqlType);
                builder.dataType(type);
            }
        }
        return builder.build();
    }
}
