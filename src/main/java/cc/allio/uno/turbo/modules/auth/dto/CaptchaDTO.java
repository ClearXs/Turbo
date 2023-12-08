package cc.allio.uno.turbo.modules.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 验证码dto
 *
 * @author j.x
 * @date 2023/10/27 13:39
 * @since 0.1.0
 */
@Data
@Schema(description = "验证码")
public class CaptchaDTO {

    /**
     * 唯一标识
     */
    @Schema(description = "验证码")
    private String captchaId;

    /**
     * base64
     */
    private String base64;
}
