package cc.allio.turbo.modules.development.code;

import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.modules.development.enums.CodeGenerateSource;
import cc.allio.turbo.modules.development.domain.BoSchema;
import cc.allio.turbo.modules.development.domain.TableColumns;
import cc.allio.turbo.modules.development.domain.hybrid.HybridBoSchema;
import cc.allio.turbo.modules.development.domain.view.DataView;
import cc.allio.turbo.modules.development.vo.CodeContent;
import cc.allio.turbo.modules.development.entity.DevCodeGenerate;
import cc.allio.turbo.modules.development.entity.DevCodeGenerateTemplate;
import cc.allio.turbo.modules.development.service.IDevDataSourceService;
import cc.allio.turbo.modules.development.vo.DataTable;
import cc.allio.turbo.modules.system.entity.SysCategory;
import cc.allio.turbo.modules.system.service.ISysCategoryService;
import cc.allio.uno.core.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

/**
 * the {@link CodeGenerateSource#DATATABLE} implementation
 *
 * @author j.x
 * @date 2024/8/4 19:26
 * @since 0.1.1
 */
@Slf4j
public class DataTableCodeGenerator implements CodeGenerator {

    private final IDevDataSourceService dataSourceService;
    private final ISysCategoryService categoryService;

    public DataTableCodeGenerator(IDevDataSourceService dataSourceService, ISysCategoryService categoryService) {
        this.dataSourceService = dataSourceService;
        this.categoryService = categoryService;
    }

    @Override
    public List<CodeContent> generate(DevCodeGenerate codeGenerate, List<DevCodeGenerateTemplate> templates) {
        String datatable = codeGenerate.getDatatable();
        DataTable dataTable = JsonUtils.parse(datatable, DataTable.class);
        Long dataSource = dataTable.getDataSource();
        String table = dataTable.getTable();
        TableColumns tableColumns;
        try {
            tableColumns = dataSourceService.showTable(dataSource, table);
        } catch (BizException ex) {
            log.error("incorrect show table command", ex);
            return Collections.emptyList();
        }
        String dataViewString = codeGenerate.getDataView();
        DataView dataView = JsonUtils.parse(dataViewString, DataView.class);
        BoSchema boSchema = BoSchema.from(tableColumns);
        HybridBoSchema hybridBoSchema =
                HybridBoSchema.composite(
                        boSchema,
                        dataView,
                        new FilterFieldColumnStrategy(codeGenerate.getIgnoreDefaultField()));
        SysCategory category = categoryService.getById(codeGenerate.getCategoryId());
        Module module = Module.from(category);
        Instance instance = Instance.from(codeGenerate);
        CodeGenerateContext codeGenerateContext = new CodeGenerateContext();
        codeGenerateContext.setModule(module);
        codeGenerateContext.setBoSchema(hybridBoSchema);
        codeGenerateContext.setInstance(instance);
        return doGenerate(codeGenerateContext, templates);
    }

    @Override
    public CodeGenerateSource getSource() {
        return CodeGenerateSource.DATATABLE;
    }
}
