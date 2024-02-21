package cc.allio.turbo.common.db.entity;

import cc.allio.uno.core.util.FieldUtils;
import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.helper.ColumnDefListResolver;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static cc.allio.turbo.common.db.entity.EntityResolver.COLUMN_DEF_RESOLVER;

/**
 * mybatis entity解析
 *
 * @author jiangwei
 * @date 2024/2/7 00:00
 * @since 0.1.0
 */
public class EntityColumnDefListResolver implements ColumnDefListResolver {

    @Override
    public List<ColumnDef> resolve(Class<?> pojoClass) {
        Field[] allFields = FieldUtils.getAllFields(pojoClass);
        return Arrays.stream(allFields)
                .map(COLUMN_DEF_RESOLVER::resolve)
                .filter(Objects::nonNull)
                .toList();
    }
}
