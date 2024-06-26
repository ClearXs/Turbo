package cc.allio.turbo.modules.developer.code;

import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.modules.developer.constant.CodeGenerateSource;
import cc.allio.turbo.modules.developer.domain.hybrid.HybridBoSchema;
import cc.allio.turbo.modules.developer.vo.CodeContent;
import cc.allio.turbo.modules.developer.entity.DevCodeGenerate;
import cc.allio.turbo.modules.developer.entity.DevCodeGenerateTemplate;
import com.baomidou.mybatisplus.core.toolkit.StringPool;

import java.util.List;

/**
 * code generator. base on {@link CodeGenerateSource} carry on difference implementation
 *
 * @author j.x
 * @date 2024/6/20 22:08
 * @since 0.1.1
 */
public interface CodeGenerator {

    /**
     * generate code to list of {@link CodeContent}
     *
     * @param codeGenerate the {@link DevCodeGenerate} instance
     * @param templates the list of {@link DevCodeGenerateTemplate}
     * @return the list of {@link CodeContent}
     */
    List<CodeContent> generate(DevCodeGenerate codeGenerate, List<DevCodeGenerateTemplate> templates) throws BizException;

    /**
     * do generate code
     *
     * @param boSchema the {@link HybridBoSchema} instance
     * @param module the {@link Module} instance
     * @param templates the list of {@link DevCodeGenerateTemplate}
     * @return the list of {@link CodeContent}
     */
    default List<CodeContent> doGenerate(HybridBoSchema boSchema, Module module, List<DevCodeGenerateTemplate> templates) {
        CodeGenerateContext codeGenerateContext = new CodeGenerateContext();
        codeGenerateContext.setModule(module);
        codeGenerateContext.setBoSchema(boSchema);
        return templates.stream()
                .map(template -> {
                    CodeContent codeContent = new CodeContent();
                    String filename = template.getFileName();
                    codeContent.setFilename(filename);
                    codeContent.setTemplate(template);
                    String templateContent = template.getContent();
                    String content = CodeTemplateParser.parse(templateContent, codeGenerateContext);
                    codeContent.setContent(content);
                    return codeContent;
                })
                .toList();
    }

    /**
     * get {@link CodeGenerateSource}
     */
    CodeGenerateSource getSource();
}
