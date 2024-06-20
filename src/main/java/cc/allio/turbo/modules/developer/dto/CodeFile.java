package cc.allio.turbo.modules.developer.dto;

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
public class CodeFile {

    /**
     * the file name
     */
    private String filename;

    /**
     * file content
     */
    private String content;

    /**
     * refer to {@link DevCodeGenerateTemplate} id
     */
    private Long templateId;

}
