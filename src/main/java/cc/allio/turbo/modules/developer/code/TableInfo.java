package cc.allio.turbo.modules.developer.code;

import cc.allio.turbo.modules.developer.domain.TableColumns;
import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.Table;

import java.util.List;

/**
 * definition code generate table information, bridge to {@link TableColumns}
 *
 * @author j.x
 * @date 2024/5/4 13:39
 * @since 0.1.1
 */
public class TableInfo {

    public final DSLName name;
    public final String comment;

    /**
     * the {@link ColumnDef} list
     */
    public final List<HybridColumn> columnDefs;

    public TableInfo(TableColumns tableColumns) {
        Table table = tableColumns.getTable();
        this.name = table.getName();
        this.comment = table.getComment();
        this.columnDefs = tableColumns.getColumnDefs().stream().map(HybridColumn::new).toList();
    }
}
