package cc.allio.turbo.modules.development.entity;

import cc.allio.turbo.common.db.entity.Entity;
import cc.allio.turbo.common.db.entity.EntityColumnDefResolver;
import cc.allio.turbo.modules.development.domain.BoAttrSchema;
import cc.allio.turbo.modules.development.domain.BoSchema;
import cc.allio.uno.core.util.concurrent.LockContext;
import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.helper.ColumnDefResolver;

import java.lang.reflect.Field;

/**
 * {@link DomainEntity}的{@link ColumnDef}的解析
 *
 * @author j.x
 * @date 2024/2/7 17:39
 * @since 0.1.0
 */
public class DomainEntityColumnDefResolver implements ColumnDefResolver {

    @Override
    public ColumnDef resolve(Field field) {
        LockContext lockContext = Entity.obtainThreadLockContext();
        if (lockContext != null) {
            return lockContext.get(DomainEntity.SCHEMA, BoSchema.class)
                    .map(BoSchema::primarySchema)
                    .flatMap(schema -> schema.obtainFieldSchema().stream().filter(f -> field.getName().equals(f.getKey())).findFirst())
                    .map(schema -> BoAttrSchema.to(schema).toColumnDef())
                    .orElse(null);
        }
        return new EntityColumnDefResolver().resolve(field);
    }
}
