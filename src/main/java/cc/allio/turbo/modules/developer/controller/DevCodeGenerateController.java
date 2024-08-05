package cc.allio.turbo.modules.developer.controller;

import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.common.web.CategoryServiceTurboCrudController;
import cc.allio.turbo.common.web.R;
import cc.allio.turbo.modules.developer.entity.DevCodeGenerate;
import cc.allio.turbo.modules.developer.service.IDevCodeGenerateService;
import cc.allio.turbo.modules.developer.vo.CodeContent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dev/code/generate")
@AllArgsConstructor
@Tag(name = "代码生成")
public class DevCodeGenerateController extends CategoryServiceTurboCrudController<DevCodeGenerate, IDevCodeGenerateService> {

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


    @GetMapping("/batch-generate/{id}")
    @Operation(summary = "生成代码")
    public void batchGenerate(@Validated @PathVariable("id") @NotNull List<Long> id, HttpServletResponse response) throws BizException {
        getService().batchGenerate(id, response);
    }
}
