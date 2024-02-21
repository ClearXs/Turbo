package cc.allio.turbo.common.web.params;

import cc.allio.turbo.common.db.mybatis.helper.MybatisKit;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.lang.reflect.Field;

@Data
@EqualsAndHashCode(callSuper = true)
public class EntityTerm extends Term {

    /**
     * 实体类型
     */
    private final Class<?> entityType;

    /**
     * 数据库表column
     */
    @Getter
    private final String tableColumn;

    /**
     * table column type
     */
    private final Class<?> columnType;

    public EntityTerm(Term term, Class<?> entityType, String tableColumn, Class<?> columnType) {
        setField(term.getField());
        setValue(term.getValue());
        this.entityType = entityType;
        this.tableColumn = tableColumn;
        this.columnType = columnType;
    }

    public EntityTerm(Term term, Class<?> entityType, Field entityField) {
        setField(term.getField());
        setValue(term.getValue());
        this.entityType = entityType;
        if (entityField != null) {
            this.tableColumn = MybatisKit.getTableColumn(entityField);
            this.columnType = entityField.getType();
        } else {
            this.tableColumn = null;
            this.columnType = null;
        }
    }
}
