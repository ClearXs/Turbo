package cc.allio.turbo.modules.developer.code;

import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.modules.developer.constant.CodeGenerateSource;
import cc.allio.turbo.modules.developer.domain.BoSchema;
import cc.allio.turbo.modules.developer.domain.hybrid.HybridBoSchema;
import cc.allio.turbo.modules.developer.domain.view.DataView;
import cc.allio.turbo.modules.developer.vo.CodeContent;
import cc.allio.turbo.modules.developer.entity.DevCodeGenerate;
import cc.allio.turbo.modules.developer.entity.DevCodeGenerateTemplate;
import cc.allio.turbo.modules.developer.entity.DevPage;
import cc.allio.turbo.modules.developer.service.IDevBoService;
import cc.allio.turbo.modules.developer.service.IDevPageService;
import cc.allio.uno.core.util.JsonUtils;

import java.util.List;

/**
 * the {@link CodeGenerateSource#PAGE} of {@link CodeGenerator} implementation. it will be use page bo schema
 *
 * @author j.x
 * @date 2024/6/20 22:21
 * @since 0.1.1
 */
public class PageCodeGenerator implements CodeGenerator {

    private final IDevPageService devPageService;
    private final IDevBoService devBoService;

    public PageCodeGenerator(IDevPageService devPageService, IDevBoService devBoService) {
        this.devPageService = devPageService;
        this.devBoService = devBoService;
    }

    @Override
    public List<CodeContent> generate(DevCodeGenerate codeGenerate, List<DevCodeGenerateTemplate> templates) throws BizException {
        Long pageId = codeGenerate.getPageId();
        DevPage page = devPageService.getById(pageId);
        if (page == null) {
            throw new BizException("code generator by 'page', but page is null");
        }
        Long boId = page.getBoId();
        BoSchema boSchema = devBoService.cacheToSchema(boId);
        if (boSchema == null) {
            throw new BizException("not found bo schema");
        }
        String dataviewString = codeGenerate.getDataView();
        DataView dataView = JsonUtils.parse(dataviewString, DataView.class);
        HybridBoSchema hybridBoSchema = HybridBoSchema.compose(boSchema, dataView);
        Module module = Module.from(codeGenerate);
        return doGenerate(hybridBoSchema, module, templates);
    }

    @Override
    public CodeGenerateSource getSource() {
        return CodeGenerateSource.PAGE;
    }
}
