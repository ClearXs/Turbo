package cc.allio.turbo.modules.auth.params;

import lombok.Data;

/**
 * corresponding {@link LoginMode#CAPTCHA_CODE} login method
 *
 * @author j.x
 * @date 2024/4/6 17:30
 * @since 0.1.1
 */
@Data
public class LoginCaptcha implements LoginClaim {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * login mode
     */
    private LoginMode loginMode;

    /**
     * the captcha code
     */
    private CaptchaCode captchaCode;
}
