package cc.allio.turbo.modules.developer.entity;

import cc.allio.turbo.common.db.entity.Entity;
import cc.allio.turbo.modules.developer.domain.BoAttrSchema;
import cc.allio.turbo.modules.developer.domain.BoSchema;
import cc.allio.uno.core.concurrent.LockContext;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.helper.TableResolver;

/**
 * {@link DomainEntity} table 解析器
 *
 * @author jiangwei
 * @date 2024/2/6 20:59
 * @since 0.1.0
 */
public class DomainEntityTableResolver implements TableResolver {

    @Override
    public Table resolve(Class<?> pojoClass) {
        LockContext lockContext = Entity.obtainThreadLockContext();
        if (lockContext != null) {
            return lockContext.get(DomainEntity.SCHEMA, BoSchema.class)
                    .map(BoSchema::primarySchema)
                    .map(schema -> Table.of(schema.getField()))
                    .orElse(null);
        }
        return null;
    }
}
