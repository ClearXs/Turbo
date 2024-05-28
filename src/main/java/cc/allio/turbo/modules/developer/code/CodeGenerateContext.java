package cc.allio.turbo.modules.developer.code;

import cc.allio.turbo.modules.developer.domain.TableColumns;
import cc.allio.uno.core.util.template.TemplateContext;
import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.type.DataType;

/**
 * definition code generate context
 *
 * @author j.x
 * @date 2024/5/3 17:07
 * @since 0.1.1
 */
public class CodeGenerateContext extends TemplateContext {

    static final String MODULE_NAME = "module";
    static final String TABLE_NAME = "table";


    public CodeGenerateContext() {
        super();
        // add mvel import class
        addImport(DSLName.class);
        addImport(ColumnDef.class);
        addImport(TableInfo.class);
        addImport(Module.class);
        addImport(DataType.class);
    }

    /**
     * set {@link Module} to context
     *
     * @param module the {@link Module} instance
     */
    public void setModule(Module module) {
        putAttribute(MODULE_NAME, module);
    }

    /**
     * set {@link TableColumns} to context
     *
     * @param tableColumns the {@link TableColumns} instance
     */
    public void setTableColumns(TableColumns tableColumns) {
        TableInfo tableInfo = new TableInfo(tableColumns);
        putAttribute(TABLE_NAME, tableInfo);
    }
}
