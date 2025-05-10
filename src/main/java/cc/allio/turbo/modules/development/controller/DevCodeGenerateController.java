package cc.allio.turbo.modules.development.controller;

import cc.allio.turbo.common.db.constant.StorageType;
import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.common.web.CategoryServiceTurboCrudController;
import cc.allio.turbo.common.web.R;
import cc.allio.turbo.modules.development.domain.TableColumns;
import cc.allio.turbo.modules.development.dto.ParseDDLDTO;
import cc.allio.turbo.modules.development.entity.DevCodeGenerate;
import cc.allio.turbo.modules.development.service.IDevCodeGenerateService;
import cc.allio.turbo.modules.development.vo.CodeContent;
import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.ddl.CreateTableOperator;
import cc.allio.uno.data.orm.dsl.opeartorgroup.Operators;
import com.alibaba.druid.sql.parser.ParserException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dev/code/generate")
@AllArgsConstructor
@Tag(name = "代码生成")
public class DevCodeGenerateController extends CategoryServiceTurboCrudController<DevCodeGenerate, IDevCodeGenerateService> {

    @PostMapping("/parse-ddl")
    @Operation(summary = "解析ddl")
    public R<TableColumns> parseDDL(@Valid @RequestBody ParseDDLDTO dto) {
        String ddlText = dto.getDdlText();
        StorageType engine = dto.getEngine();
        CreateTableOperator<?> createTableOperator = Operators.getCreateTableOperator(CreateTableOperator.class, OperatorKey.SQL, engine.toDbType());
        try {
            createTableOperator.parse(ddlText);
        } catch (ParserException e) {
            return R.internalError(e);
        }
        Table table = createTableOperator.getTable();
        List<ColumnDef> columns = createTableOperator.getColumns();
        TableColumns tableColumns = new TableColumns(table);
        tableColumns.addAllColumns(columns);
        return R.ok(tableColumns);
    }

    @GetMapping("/preview/{id}")
    @Operation(summary = "预览")
    public R<List<CodeContent>> preview(@Validated @PathVariable("id") @NotNull Long id) throws BizException {
        List<CodeContent> codeContentList = getService().preview(id);
        return R.ok(codeContentList);
    }

    @GetMapping("/generate/{id}")
    @Operation(summary = "生成代码")
    public void generate(@Validated @PathVariable("id") @NotNull Long id, HttpServletResponse response) throws BizException {
        getService().generate(id, response);
    }

    @GetMapping("/generate/{id}/{filename}")
    @Operation(summary = "生成指定文件代码")
    public void generateFile(@Validated @PathVariable("id") @NotNull Long id, @PathVariable @NotNull String filename, HttpServletResponse response) throws BizException {
        getService().generateFile(id, filename, response);
    }

    @GetMapping("/batch-generate/{id}")
    @Operation(summary = "生成代码")
    public void batchGenerate(@Validated @PathVariable("id") @NotNull List<Long> id, HttpServletResponse response) throws BizException {
        getService().batchGenerate(id, response);
    }
}
