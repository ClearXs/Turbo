package cc.allio.turbo.modules.developer.vo;

import cc.allio.turbo.modules.developer.entity.DevCodeGenerate;
import cc.allio.turbo.modules.developer.entity.DevCodeGenerateTemplate;
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
     * file content
     */
    private String content;

    /**
     * refer to {@link DevCodeGenerateTemplate}
     */
    private DevCodeGenerateTemplate template;

}
