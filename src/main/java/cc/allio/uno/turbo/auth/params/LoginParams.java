package cc.allio.uno.turbo.auth.params;

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
     * 租户id
     */
    private String tenant;

    /**
     * 验证码id
     */
    private String captchaId;

    /**
     * 验证码code
     */
    private String captcha;

    /**
     * rememberMe
     */
    private boolean rememberMe;
}
