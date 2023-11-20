package cc.allio.uno.turbo.common.i18n;

/**
 * 格式化code
 *
 * @author j.x
 * @date 2023/10/27 18:15
 * @since 1.0.0
 */
public final class ExceptionCodes {

    // ============================== system ==============================

    /**
     * 系统通用错误 '操作失败'
     */
    public static final String OPERATE_ERROR = "error.system.operate.error";

    // ============================== auth ==============================

    /**
     * 认证失败
     */
    public static final String AUTHENTICATION_FAILED = "error.auth.authentication.failed";

    /**
     * 访问拒绝
     */
    public static final String ACCESS_DENIED = "error.auth.access.denied";

    /**
     * token expire
     */
    public static final String TOKEN_EXPIRED = "error.auth.token.expired";

    /**
     * 用户名或者密码错误
     */
    public static final String USERNAME_OR_PASSWORD_FAILED = "error.auth.username.failed";

    /**
     * 验证码过期
     */
    public static final String CAPTCHA_EXPIRED = "error.auth.captcha.expired";

    /**
     * 验证码错误
     */
    public static final String CAPTCHA_ERROR = "error.auth.captcha.error";

    // ============================== menu ==============================

    /**
     * 用户未找到
     */
    public static final String USER_NOT_FOUND = "error.user.not.found";

    /**
     * 用户重复
     */
    public static final String USER_REPEAT = "error.user.repeat";

    // ============================== menu ==============================

    /**
     * 菜单删除
     */
    public static final String MENU_DELETE_FAILED = "error.menu.delete.failed";

    // ============================== attachment ==============================

    /**
     * 上传失败
     */
    public static final String ATTACHMENT_UPLOAD_ERROR = "error.attachment.upload.error";
    public static final String ATTACHMENT_UPLOAD_EXCEED_SIZE = "error.attachment.upload.exceed.size";
    public static final String ATTACHMENT_UPLOAD_EXECUTOR_NOTFOUND = "error.attachment.upload.executor.notfound";
    public static final String ATTACHMENT_DOWNLOAD_ERROR = "error.attachment.download.error";

}
