package cc.allio.turbo.modules.developer.code;

import cc.allio.turbo.common.api.Key;
import cc.allio.turbo.modules.developer.domain.view.FieldColumn;
import cc.allio.turbo.modules.developer.entity.DomainEntity;
import com.google.common.collect.Sets;

import java.util.Set;
import java.util.function.Predicate;

public class FilterFieldColumnStrategy implements Predicate<FieldColumn> {

    private final Boolean ignoreDefaultField;

    private static final Set<String> DEFAULT_FIELDS = Sets.newHashSet();

    static {
        DEFAULT_FIELDS.add(new Key(DomainEntity.ID).getHump());
        DEFAULT_FIELDS.add(new Key(DomainEntity.CREATED_BY).getHump());
        DEFAULT_FIELDS.add(new Key(DomainEntity.UPDATED_BY).getHump());
        DEFAULT_FIELDS.add(new Key(DomainEntity.UPDATED_TIME).getHump());
        DEFAULT_FIELDS.add(new Key(DomainEntity.CREATED_TIME).getHump());
        DEFAULT_FIELDS.add(new Key(DomainEntity.IS_DELETED).getHump());
        DEFAULT_FIELDS.add(new Key(DomainEntity.VERSION).getHump());
        DEFAULT_FIELDS.add(new Key(DomainEntity.TENANT_ID).getHump());
    }

    public FilterFieldColumnStrategy(Boolean ignoreDefaultField) {
        this.ignoreDefaultField = ignoreDefaultField;
    }

    @Override
    public boolean test(FieldColumn fieldColumn) {
        if (Boolean.TRUE.equals(ignoreDefaultField)) {
            String field = fieldColumn.getField();
            return DEFAULT_FIELDS.contains(field);
        }
        return false;
    }
}
