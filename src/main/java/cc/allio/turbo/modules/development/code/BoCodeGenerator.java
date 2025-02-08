package cc.allio.turbo.modules.development.code;

import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.modules.development.enums.CodeGenerateSource;
import cc.allio.turbo.modules.development.domain.BoSchema;
import cc.allio.turbo.modules.development.domain.hybrid.HybridBoSchema;
import cc.allio.turbo.modules.development.domain.view.DataView;
import cc.allio.turbo.modules.development.entity.DevCodeGenerate;
import cc.allio.turbo.modules.development.entity.DevCodeGenerateTemplate;
import cc.allio.turbo.modules.development.service.IDevBoService;
import cc.allio.turbo.modules.development.vo.CodeContent;
import cc.allio.turbo.modules.system.entity.SysCategory;
import cc.allio.turbo.modules.system.service.ISysCategoryService;
import cc.allio.uno.core.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

/**
 * the {@link CodeGenerateSource} implementation.
 *
 * @author j.x
 * @date 2024/8/4 19:27
 * @since 0.1.1
 */
@Slf4j
public class BoCodeGenerator implements CodeGenerator {

    private final ISysCategoryService categoryService;
    private final IDevBoService devBoService;

    public BoCodeGenerator(ISysCategoryService categoryService, IDevBoService devBoService) {
        this.categoryService = categoryService;
        this.devBoService = devBoService;
    }

    @Override
    public List<CodeContent> generate(DevCodeGenerate codeGenerate, List<DevCodeGenerateTemplate> templates) {
        Long boId = codeGenerate.getBoId();
        BoSchema boSchema;
        try {
            boSchema = devBoService.cacheToSchema(boId);
        } catch (BizException ex) {
            log.error("not find business object", ex);
            return Collections.emptyList();
        }
        if (boSchema == null) {
            log.error("not found bo schema");
            return Collections.emptyList();
        }
        String dataviewString = codeGenerate.getDataView();
        DataView dataView = JsonUtils.parse(dataviewString, DataView.class);
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
        return CodeGenerateSource.BO;
    }
}
