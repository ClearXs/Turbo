package cc.allio.turbo.modules.development.code;

import cc.allio.turbo.common.db.constant.FieldType;
import cc.allio.turbo.modules.development.domain.BoAttrSchema;
import cc.allio.uno.data.orm.dsl.type.TypeRegistry;

/**
 * code generate utility method
 *
 * @author j.x
 * @date 2024/6/30 18:48
 * @since 0.1.1
 */
public class CodeGenerateHelper {

    /**
     * from the {@link BoAttrSchema} get Java Class type
     *
     * @param attr the {@link BoAttrSchema} instance
     * @return Java class type
     */
    public static Class<?> getJavaType(BoAttrSchema attr) {
        FieldType type = attr.getType();
        if (type == null) {
            return Object.class;
        }
        int jdbcType = type.getDslType().getJdbcType();
        return TypeRegistry.getInstance().findJavaType(jdbcType).getJavaType();
    }
}
