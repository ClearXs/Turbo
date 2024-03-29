package cc.allio.turbo.common.db.entity;

import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.helper.TableResolver;
import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * 解析mybatis table
 *
 * @author j.x
 * @date 2024/2/6 23:58
 * @since 0.1.0
 */
public class EntityTableResolver implements TableResolver {

    @Override
    public Table resolve(Class<?> pojoClass) {
        TableName tableName = AnnotationUtils.findAnnotation(pojoClass, TableName.class);
        if (tableName != null) {
            Table table = new Table();
            table.setName(DSLName.of(tableName.value(), DSLName.UNDERLINE_FEATURE));
            table.setSchema(tableName.schema());
            return table;
        }
        return null;
    }
}
