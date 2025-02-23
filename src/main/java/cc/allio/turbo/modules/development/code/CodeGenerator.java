package cc.allio.turbo.modules.development.code;

import cc.allio.turbo.modules.development.enums.CodeGenerateSource;
import cc.allio.turbo.modules.development.vo.CodeContent;
import cc.allio.turbo.modules.development.entity.DevCodeGenerate;
import cc.allio.turbo.modules.development.entity.DevCodeGenerateTemplate;

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
     * generate code to one of {@link CodeContent}
     *
     * @param codeGenerate the {@link DevCodeGenerate} instance
     * @param template     the  {@link DevCodeGenerateTemplate}
     * @return the list of {@link CodeContent}
     */
    default CodeContent generateOne(DevCodeGenerate codeGenerate, DevCodeGenerateTemplate template) {
        return generate(codeGenerate, List.of(template)).stream().findFirst().orElse(null);
    }

    /**
     * generate code to list of {@link CodeContent}
     *
     * @param codeGenerate the {@link DevCodeGenerate} instance
     * @param templates    the list of {@link DevCodeGenerateTemplate}
     * @return the list of {@link CodeContent}
     */
    List<CodeContent> generate(DevCodeGenerate codeGenerate, List<DevCodeGenerateTemplate> templates);

    /**
     * do generate code
     *
     * @param codeGenerateContext the {@link CodeGenerateContext}
     * @param template            the list of {@link DevCodeGenerateTemplate}
     * @return the list of {@link CodeContent}
     */
    default CodeContent doGenerateOne(CodeGenerateContext codeGenerateContext, DevCodeGenerateTemplate template) {
        CodeContent codeContent = new CodeContent();
        String templateFileName = template.getFileName();
        codeContent.setFilename(templateFileName);
        codeContent.setTemplate(template);
        codeContent.setLanguage(template.getLanguage());
        String templateContent = template.getContent();
        String content = CodeTemplateParser.parse(templateContent, codeGenerateContext);
        codeContent.setContent(content);
        String filename = CodeTemplateParser.parse(templateFileName, codeGenerateContext);
        codeContent.setFilename(filename);
        String templateFilePath = template.getFilePath();
        String filepath = CodeTemplateParser.parse(templateFilePath, codeGenerateContext);
        codeContent.setFilepath(filepath);
        codeContent.setCodeDomain(template.getDomain());
        return codeContent;
    }

    /**
     * do generate code
     *
     * @param codeGenerateContext the {@link CodeGenerateContext}
     * @param templates           the list of {@link DevCodeGenerateTemplate}
     * @return the list of {@link CodeContent}
     */
    default List<CodeContent> doGenerate(CodeGenerateContext codeGenerateContext, List<DevCodeGenerateTemplate> templates) {
        return templates.stream()
                .map(template -> doGenerateOne(codeGenerateContext, template))
                .toList();
    }

    /**
     * get {@link CodeGenerateSource}
     */
    CodeGenerateSource getSource();
}
