package cc.allio.turbo.modules.development.code;

import cc.allio.turbo.modules.development.domain.hybrid.HybridBoSchema;
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
    static final String INSTANCE_NAME = "instance";
    static final String BO_SCHEMA_NAME = "schema";

    public CodeGenerateContext() {
        super();
        // add mvel import class
        addImport(DSLName.class);
        addImport(ColumnDef.class);
        addImport(Module.class);
        addImport(Instance.class);
        addImport(DataType.class);
        addImport(HybridBoSchema.class);
        addImport("helper", CodeGenerateHelper.class);
    }

    /**
     * set {@link Module} to context
     *
     * @param module the {@link Module} instance
     */
    public void setModule(Module module) {
        put(MODULE_NAME, module);
    }

    /**
     * set {@link HybridBoSchema} to context
     *
     * @param boSchema the {@link HybridBoSchema} instance
     */
    public void setBoSchema(HybridBoSchema boSchema) {
        put(BO_SCHEMA_NAME, boSchema);
    }

    /**
     * set {@link Instance} to context
     *
     * @param instance the instance
     */
    public void setInstance(Instance instance) {
        put(INSTANCE_NAME, instance);
    }
}
