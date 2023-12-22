package cc.allio.turbo.modules.auth.exception;

import org.springframework.security.core.AuthenticationException;

public class CaptchaError extends AuthenticationException {

    public CaptchaError() {
        super("");
    }

    public CaptchaError(String msg, Throwable cause) {
        super(msg, cause);
    }
}
