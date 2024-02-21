package cc.allio.turbo.modules.developer.domain;

import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.Table;
import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.List;

@Getter
public final class TableColumns {

    private final DSLName tableName;
    private final Table table;
    private final List<ColumnDef> columnDefs = Lists.newArrayList();

    public TableColumns(Table table) {
        this.table = table;
        this.tableName = table.getName();
    }

    /**
     * 添加 column
     */
    public void addColumn(ColumnDef columnDef) {
        this.columnDefs.add(columnDef);
    }

    /**
     * 批量添加
     *
     * @param columnDefs columnDefs
     */
    public void addAllColumns(List<ColumnDef> columnDefs) {
        this.columnDefs.addAll(columnDefs);
    }
}
