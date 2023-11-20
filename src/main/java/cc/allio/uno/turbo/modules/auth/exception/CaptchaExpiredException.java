package cc.allio.uno.turbo.modules.auth.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码过期
 *
 * @author j.x
 * @date 2023/10/27 22:38
 * @since 1.0.0
 */
public class CaptchaExpiredException extends AuthenticationException {

    public CaptchaExpiredException() {
        super("");
    }

    public CaptchaExpiredException(String msg) {
        super(msg);
    }
}
