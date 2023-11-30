package cc.allio.uno.turbo.common.web.params;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.lang.reflect.Field;

@Data
@EqualsAndHashCode(callSuper = true)
public class EntityTerm extends Term {

    /**
     * 实体类型
     */
    private final Class<?> entityType;

    /**
     * 对应term条件下对应实体的{@link Field}实例
     */
    private final Field entityField;

    public EntityTerm(Term term, Class<?> entityType, Field entityField) {
        setField(term.getField());
        setValue(term.getValue());
        this.entityType = entityType;
        this.entityField = entityField;
    }
}
