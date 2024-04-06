package cc.allio.turbo.modules.auth.params;

import lombok.Data;

@Data
public class CaptchaCode {

    /**
     * 验证码内容
     */
    private String captcha;

    /**
     * 验证码id
     */
    private String captchaId;
}
