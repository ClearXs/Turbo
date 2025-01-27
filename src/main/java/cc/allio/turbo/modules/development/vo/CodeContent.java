package cc.allio.turbo.modules.development.vo;

import cc.allio.turbo.modules.development.constant.CodeLanguage;
import cc.allio.turbo.modules.development.constant.CodeTemplateDomain;
import cc.allio.turbo.modules.development.entity.DevCodeGenerate;
import cc.allio.turbo.modules.development.entity.DevCodeGenerateTemplate;
import lombok.Data;

/**
 * description {@link DevCodeGenerate} generative result
 *
 * @author j.x
 * @date 2024/6/16 19:43
 * @since 0.1.1
 */
@Data
public class CodeContent {

    /**
     * the file name
     */
    private String filename;

    /**
     * the file path
     */
    private String filepath;

    /**
     * the file content
     */
    private String content;

    /**
     * code language
     */
    private CodeLanguage language;

    /**
     * code domain
     */
    private CodeTemplateDomain codeDomain;

    /**
     * refer to {@link DevCodeGenerateTemplate}
     */
    private DevCodeGenerateTemplate template;
}
