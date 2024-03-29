package cc.allio.turbo.modules.developer.entity;

import cc.allio.turbo.common.db.entity.Entity;
import cc.allio.turbo.modules.developer.domain.BoAttrSchema;
import cc.allio.turbo.modules.developer.domain.BoAttributeTree;
import cc.allio.turbo.modules.developer.domain.BoSchema;
import cc.allio.uno.core.concurrent.LockContext;
import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.helper.ColumnDefListResolver;

import java.util.Collections;
import java.util.List;

/**
 * {@link DomainEntity}的{@link ColumnDef}的解析器
 *
 * @author j.x
 * @date 2024/2/6 21:25
 * @since 0.1.0
 */
public class DomainEntityColumnDefListResolver implements ColumnDefListResolver {

    @Override
    public List<ColumnDef> resolve(Class<?> pojoClass) {
        LockContext lockContext = Entity.obtainThreadLockContext();
        if (lockContext != null) {
            return lockContext.get(DomainEntity.SCHEMA, BoSchema.class)
                    .map(BoSchema::primarySchema)
                    .map(attrSchema -> {
                        List<BoAttrSchema> fieldSchema = attrSchema.obtainFieldSchema();
                        return fieldSchema
                                .stream()
                                .map(BoAttrSchema::to)
                                .map(BoAttributeTree::toColumnDef)
                                .toList();
                    })
                    .orElse(Collections.emptyList());
        }
        return Collections.emptyList();
    }
}
