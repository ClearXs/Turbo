package cc.allio.turbo.modules.development.entity;

import cc.allio.uno.data.orm.dsl.helper.PojoInspect;

public class DomainEntityPojoInspect implements PojoInspect {
    @Override
    public boolean isPojo(Class<?> maybePojo) {
        return DomainEntity.class.isAssignableFrom(maybePojo);
    }

    @Override
    public boolean useCache() {
        return false;
    }
}
