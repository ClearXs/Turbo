package cc.allio.turbo.modules.auth.params;

import lombok.Data;

@Data
public class LoginParams {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 验证码id
     */
    private String captchaId;

    /**
     * 验证码code
     */
    private String captcha;

}
