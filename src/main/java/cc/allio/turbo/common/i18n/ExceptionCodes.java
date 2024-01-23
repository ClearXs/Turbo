package cc.allio.turbo.common.i18n;

/**
 * 异常codes
 *
 * @author j.x
 * @date 2023/10/27 18:15
 * @since 0.1.0
 */
public final class ExceptionCodes {
    private ExceptionCodes() {
    }

    private static final String GROUP = "exception";
    private static final String SYSTEM_GROUP = "system";
    private static final String AUTH_GROUP = "auth";
    private static final String USER_GROUP = "user";
    private static final String MENU_GROUP = "menu";
    private static final String ATTACHMENT_GROUP = "attachment";

    public static final I18nCode OPERATE_ERROR = I18nCode.of("error.system.operate.error", "系统通用错误 '操作失败'", GROUP, SYSTEM_GROUP);
    public static final I18nCode AUTHENTICATION_FAILED = I18nCode.of("error.auth.authentication.failed", "认证失败", GROUP, AUTH_GROUP);
    public static final I18nCode ACCESS_DENIED = I18nCode.of("error.auth.access.denied", "访问拒绝", GROUP, AUTH_GROUP);
    public static final I18nCode TOKEN_EXPIRED = I18nCode.of("error.auth.token.expired", "token expire", GROUP, AUTH_GROUP);
    public static final I18nCode USERNAME_OR_PASSWORD_FAILED = I18nCode.of("error.auth.username.failed", "用户名或者密码错误", GROUP, AUTH_GROUP);
    public static final I18nCode CAPTCHA_EXPIRED = I18nCode.of("error.auth.captcha.expired", "验证码过期", GROUP, AUTH_GROUP);
    public static final I18nCode CAPTCHA_ERROR = I18nCode.of("error.auth.captcha.error", "验证码错误", GROUP, AUTH_GROUP);
    public static final I18nCode USER_NOT_FOUND = I18nCode.of("error.user.not.found", "用户未找到", GROUP, USER_GROUP);
    public static final I18nCode USER_REPEAT = I18nCode.of("error.user.repeat", "用户重复", GROUP, USER_GROUP);
    public static final I18nCode USER_RAW_PASSWORD_MISTAKE = I18nCode.of("error.user.rawpassword.mistake", "用户原密码不一致", GROUP, USER_GROUP);
    public static final I18nCode MENU_DELETE_FAILED = I18nCode.of("error.menu.delete.failed", "菜单删除", GROUP, MENU_GROUP);
    public static final I18nCode ATTACHMENT_DOWNLOAD_ERROR = I18nCode.of("error.attachment.download.error", "上传失败", GROUP, ATTACHMENT_GROUP);
    public static final I18nCode ATTACHMENT_UPLOAD_ERROR = I18nCode.of("error.attachment.upload.error", "上传失败", GROUP, ATTACHMENT_GROUP);
    public static final I18nCode ATTACHMENT_UPLOAD_EXCEED_SIZE = I18nCode.of("error.attachment.upload.exceed.size", "", GROUP, ATTACHMENT_GROUP);
    public static final I18nCode ATTACHMENT_UPLOAD_EXECUTOR_NOTFOUND = I18nCode.of("error.attachment.upload.executor.notfound", "", GROUP, ATTACHMENT_GROUP);


}
