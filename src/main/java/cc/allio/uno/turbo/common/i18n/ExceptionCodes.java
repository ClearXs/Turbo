package cc.allio.uno.turbo.common.i18n;

/**
 * 格式化code
 *
 * @author j.x
 * @date 2023/10/27 18:15
 * @since 1.0.0
 */
public final class ExceptionCodes {

    // ============================== auth ==============================

    /**
     * 认证失败
     */
    public static final String AUTHENTICATION_FAILED = "auth.authentication.failed";

    /**
     * 访问拒绝
     */
    public static final String ACCESS_DENIED = "auth.access.denied";

    /**
     * token expire
     */
    public static final String TOKEN_EXPIRED = "auth.token.expired";

    /**
     * 用户名或者密码错误
     */
    public static final String USERNAME_OR_PASSWORD_FAILED = "auth.username.failed";

    /**
     * 验证码过期
     */
    public static final String CAPTCHA_EXPIRED = "auth.captcha.expired";

    /**
     * 验证码错误
     */
    public static final String CAPTCHA_ERROR = "auth.captcha.error";
}
