package cc.allio.turbo.modules.developer.code;

import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.modules.developer.constant.CodeGenerateSource;
import cc.allio.turbo.modules.developer.domain.BoSchema;
import cc.allio.turbo.modules.developer.domain.TableColumns;
import cc.allio.turbo.modules.developer.domain.hybrid.HybridBoSchema;
import cc.allio.turbo.modules.developer.domain.view.DataView;
import cc.allio.turbo.modules.developer.vo.CodeContent;
import cc.allio.turbo.modules.developer.entity.DevCodeGenerate;
import cc.allio.turbo.modules.developer.entity.DevCodeGenerateTemplate;
import cc.allio.turbo.modules.developer.service.IDevDataSourceService;
import cc.allio.turbo.modules.developer.vo.DataTable;
import cc.allio.uno.core.util.JsonUtils;

import java.util.List;

public class DataTableCodeGenerator implements CodeGenerator {

    private final IDevDataSourceService dataSourceService;

    public DataTableCodeGenerator(IDevDataSourceService dataSourceService) {
        this.dataSourceService = dataSourceService;
    }

    @Override
    public List<CodeContent> generate(DevCodeGenerate codeGenerate, List<DevCodeGenerateTemplate> templates) throws BizException {
        String datatable = codeGenerate.getDatatable();
        DataTable dataTable = JsonUtils.parse(datatable, DataTable.class);
        Long dataSource = dataTable.getDataSource();
        String table = dataTable.getTable();
        TableColumns tableColumns = dataSourceService.showTable(dataSource, table);
        String dataViewString = codeGenerate.getDataView();
        DataView dataView = JsonUtils.parse(dataViewString, DataView.class);
        BoSchema boSchema = BoSchema.from(tableColumns);
        HybridBoSchema hybridBoSchema = HybridBoSchema.compose(boSchema, dataView);
        Module module = Module.from(codeGenerate);
        return doGenerate(hybridBoSchema, module, templates);
    }

    @Override
    public CodeGenerateSource getSource() {
        return CodeGenerateSource.DATATABLE;
    }
}
