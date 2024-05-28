package cc.allio.turbo.modules.developer.code;

import cc.allio.turbo.modules.developer.domain.TableColumns;
import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EntityTemplateParseTest extends BaseTestCase {

    TableColumns tableColumns;
    CodeGenerateContext context;

    @BeforeEach
    void init() {
        Table table = new Table();
        table.setName(DSLName.of("dual"));
        table.setComment("test");
        tableColumns = new TableColumns(table);

        ColumnDef c1 = ColumnDef.builder()
                .dslName(DSLName.of("create_name"))
                .comment("Create Name")
                .build();
        tableColumns.addColumn(c1);

        ColumnDef c2 = ColumnDef.builder()
                .dslName(DSLName.of("update_time"))
                .comment("Update Name")
                .build();
        tableColumns.addColumn(c2);

        context = new CodeGenerateContext();
        Module module = new Module();
        module.setName("Test");
        module.setKey("Test");
        module.setPackagePath("cc.allio.turbo.modules.developer");
        module.setRequestPath("/dev/test");
        module.setAuthor("j.x");
        module.setVersion("1.0.0");
        context.setModule(module);
        context.setTableColumns(tableColumns);
    }

    @Test
    void testEntityTemplate() {
        String template = "package @{module.packagePath}.entity;\n" +
                "\n" +
                "import cc.allio.turbo.common.db.constraint.Sortable;\n" +
                "import cc.allio.turbo.common.db.constraint.Unique;\n" +
                "import cc.allio.turbo.common.db.entity.TreeEntity;\n" +
                "import com.baomidou.mybatisplus.annotation.TableField;\n" +
                "import com.baomidou.mybatisplus.annotation.TableName;\n" +
                "import io.swagger.v3.oas.annotations.media.Schema;\n" +
                "import lombok.Data;\n" +
                "import lombok.EqualsAndHashCode;\n" +
                "\n" +
                "/**\n" +
                " * @{module.name}\n" +
                " *\n" +
                " * @author @{module.author}\n" +
                " * @date @{date.formatNow()}\n" +
                " * @since @{module.version}\n" +
                " */\n" +
                "@EqualsAndHashCode(callSuper = true)\n" +
                "@Data\n" +
                "@TableName(\"@{table.name.formatUnderlie()}\")\n" +
                "@Schema(description = \"@{module.name}\")\n" +
                "public class @{module.key} extends TenantEntity {\n" +
                "  @foreach{column: table.columnDefs}\n" +
                "    /**\n" +
                "     * @{column.comment}\n" +
                "     */\n" +
                "    @TableField(\"@{column.name.formatUnderlie()}\")\n" +
                "    @Schema(description = \"@{column.comment}\")\n" +
                "    private @{column.dataType.getSimpleName()} @{column.name.formatHump()};\n" +
                "  @end{}\n" +
                "}";

        String parsed = TableTemplateParser.parse(template, context);

        assertEquals("package cc.allio.turbo.modules.developer.entity;\n" +
                "\n" +
                "import cc.allio.turbo.common.db.constraint.Sortable;\n" +
                "import cc.allio.turbo.common.db.constraint.Unique;\n" +
                "import cc.allio.turbo.common.db.entity.TreeEntity;\n" +
                "import com.baomidou.mybatisplus.annotation.TableField;\n" +
                "import com.baomidou.mybatisplus.annotation.TableName;\n" +
                "import io.swagger.v3.oas.annotations.media.Schema;\n" +
                "import lombok.Data;\n" +
                "import lombok.EqualsAndHashCode;\n" +
                "\n" +
                "/**\n" +
                " * Test\n" +
                " *\n" +
                " * @author j.x\n" +
                " * @date 2024-05-04 15:36:38\n" +
                " * @since 1.0.0\n" +
                " */\n" +
                "@EqualsAndHashCode(callSuper = true)\n" +
                "@Data\n" +
                "@TableName(\"dual\")\n" +
                "@Schema(description = \"Test\")\n" +
                "public class Test extends TenantEntity {\n" +
                "  \n" +
                "    /**\n" +
                "     * Create Name\n" +
                "     */\n" +
                "    @TableField(\"create_name\")\n" +
                "    @Schema(description = \"Create Name\")\n" +
                "    private Object createName;\n" +
                "  \n" +
                "    /**\n" +
                "     * Update Name\n" +
                "     */\n" +
                "    @TableField(\"update_time\")\n" +
                "    @Schema(description = \"Update Name\")\n" +
                "    private Object updateTime;\n" +
                "  \n" +
                "}", parsed);
    }

    @Test
    void testIServiceTemplate() {
        String template = "package @{module.packagePath}.service;\n" +
                "\n" +
                "import cc.allio.turbo.common.db.uno.repository.ITurboCrudRepositoryService;\n" +
                "import @{module.packagePath}.entity.@{module.key};\n" +
                "\n" +
                "public interface I@{module.key}Service extends ITurboCrudRepositoryService<@{module.key}> {\n" +
                "}";
        String parsed = TableTemplateParser.parse(template, context);
        assertEquals("package cc.allio.turbo.modules.developer.service;\n" +
                "\n" +
                "import cc.allio.turbo.common.db.uno.repository.ITurboCrudRepositoryService;\n" +
                "import cc.allio.turbo.modules.developer.entity.Test;\n" +
                "\n" +
                "public interface ITestService extends ITurboCrudRepositoryService<Test> {\n" +
                "}", parsed);
    }

    @Test
    void testServiceImplTemplate() {
        String template = "package @{module.packagePath}.service.impl;\n" +
                "\n" +
                "import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl;\n" +
                "import @{module.packagePath}.entity.@{module.key};\n" +
                "import @{module.packagePath}.service.I@{module.key}Service;\n" +
                "import org.springframework.stereotype.Service;\n" +
                "\n" +
                "@Service\n" +
                "public class @{module.key}ServiceImpl extends SimpleTurboCrudRepositoryServiceImpl<@{module.key}> implements I@{module.key}Service {\n" +
                "}\n";
        String parsed = TableTemplateParser.parse(template, context);

        assertEquals("package cc.allio.turbo.modules.developer.service.impl;\n" +
                "\n" +
                "import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl;\n" +
                "import cc.allio.turbo.modules.developer.entity.Test;\n" +
                "import cc.allio.turbo.modules.developer.service.ITestService;\n" +
                "import org.springframework.stereotype.Service;\n" +
                "\n" +
                "@Service\n" +
                "public class TestServiceImpl extends SimpleTurboCrudRepositoryServiceImpl<Test> implements ITestService {\n" +
                "}\n", parsed);
    }

    @Test
    void testControllerTemplate() {
        String template = "package @{module.packagePath}.controller;\n" +
                "\n" +
                "import cc.allio.turbo.common.web.GenericTurboCrudController;\n" +
                "import @{module.packagePath}.entity.@{module.key};\n" +
                "import io.swagger.v3.oas.annotations.tags.Tag;\n" +
                "import lombok.AllArgsConstructor;\n" +
                "import org.springframework.web.bind.annotation.*;\n" +
                "\n" +
                "@RestController\n" +
                "@RequestMapping(\"@{module.requestPath}\")\n" +
                "@AllArgsConstructor\n" +
                "@Tag(name = \"@{module.name}\")\n" +
                "public class @{module.key}Controller extends GenericTurboCrudController<@{module.key}> {\n" +
                "\n" +
                "}\n";
        String parsed = TableTemplateParser.parse(template, context);
        assertEquals("package cc.allio.turbo.modules.developer.controller;\n" +
                "\n" +
                "import cc.allio.turbo.common.web.GenericTurboCrudController;\n" +
                "import cc.allio.turbo.modules.developer.entity.Test;\n" +
                "import io.swagger.v3.oas.annotations.tags.Tag;\n" +
                "import lombok.AllArgsConstructor;\n" +
                "import org.springframework.web.bind.annotation.*;\n" +
                "\n" +
                "@RestController\n" +
                "@RequestMapping(\"/dev/test\")\n" +
                "@AllArgsConstructor\n" +
                "@Tag(name = \"Test\")\n" +
                "public class TestController extends GenericTurboCrudController<Test> {\n" +
                "\n" +
                "}\n", parsed);
    }

    @Test
    void testApi() {
        String template = "import useRequest from '@/hook/request';\n" +
                "import { GeneralApi, GeneralApiImpl, TenantEntity } from '..';\n" +
                "\n" +
                "export interface @{module.name} extends TenantEntity {\n" +
                "\n" +
                "  @foreach{column: table.columnDefs}\n" +
                "  /**\n" +
                "   * @{column.comment}\n" +
                "   */\n" +
                "  @{column.name.formatHump()}: any;\n" +
                "  @end{}\n" +
                "}\n" +
                "\n" +
                "export interface @{module.key}Api extends GeneralApi<@{module.key}> {}\n" +
                "\n" +
                "class @{module.key}ApiImpl extends GeneralApiImpl<@{module.key}> implements @{module.key}Api {}\n" +
                "\n" +
                "export default function use@{module.key}Api(): @{module.key}Api {\n" +
                "  const request = useRequest();\n" +
                "  return new @{module.key}ApiImpl('/api@{module.requestPath}', request);\n" +
                "}\n";
        String parsed = TableTemplateParser.parse(template, context);
        System.out.println(parsed);
    }

    @Test
    void testHelper() {
        String template = "import use@{module.key}Api, { @{module.key}, @{module.key}Api } from '@/api/@{module.system}/@{module.name.toLowerCase()}';\n" +
                "import { TableColumnProps } from '@/components/TableCrud/interface';\n" +
                "import { Helper } from '@/components/interface';\n" +
                "\n" +
                "const @{module.key}Helper: Helper<@{module.key}, @{module.key}Api> = {\n" +
                "  getColumns: () => {\n" +
                "    return [\n" +
                "      @foreach{column: table.columnDefs}\n" +
                "      {\n" +
                "        label: '@{column.comment}',\n" +
                "        field: '@{column.name.formatHump()}',\n" +
                "        ellipsis: true,\n" +
                "        align: 'center',\n" +
                "        search: true,\n" +
                "        type: 'input',\n" +
                "        require: true,\n" +
                "      },\n" +
                "      @end{}\n" +
                "    ] as TableColumnProps<@{module.key}>[];\n" +
                "  },\n" +
                "\n" +
                "  wrap: (entity) => {\n" +
                "    return {\n" +
                "      key: entity.id,\n" +
                "      value: entity.id,\n" +
                "      label: entity.name,\n" +
                "    };\n" +
                "  },\n" +
                "  getApi: use@{module.key}Api,\n" +
                "};\n" +
                "\n" +
                "export default @{module.key}Helper;\n";
        String parsed = TableTemplateParser.parse(template, context);
        assertEquals("import useTestApi, { Test, TestApi } from '@/api/null/test';\n" +
                "import { TableColumnProps } from '@/components/TableCrud/interface';\n" +
                "import { Helper } from '@/components/interface';\n" +
                "\n" +
                "const TestHelper: Helper<Test, TestApi> = {\n" +
                "  getColumns: () => {\n" +
                "    return [\n" +
                "      \n" +
                "      {\n" +
                "        label: 'Create Name',\n" +
                "        field: 'createName',\n" +
                "        ellipsis: true,\n" +
                "        align: 'center',\n" +
                "        search: true,\n" +
                "        type: 'input',\n" +
                "        require: true,\n" +
                "      },\n" +
                "      \n" +
                "      {\n" +
                "        label: 'Update Name',\n" +
                "        field: 'updateTime',\n" +
                "        ellipsis: true,\n" +
                "        align: 'center',\n" +
                "        search: true,\n" +
                "        type: 'input',\n" +
                "        require: true,\n" +
                "      },\n" +
                "      \n" +
                "    ] as TableColumnProps<Test>[];\n" +
                "  },\n" +
                "\n" +
                "  wrap: (entity) => {\n" +
                "    return {\n" +
                "      key: entity.id,\n" +
                "      value: entity.id,\n" +
                "      label: entity.name,\n" +
                "    };\n" +
                "  },\n" +
                "  getApi: useTestApi,\n" +
                "};\n" +
                "\n" +
                "export default TestHelper;\n", parsed);
    }

}
