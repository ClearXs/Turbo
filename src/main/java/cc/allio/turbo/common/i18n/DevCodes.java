package cc.allio.turbo.common.i18n;

/**
 * turbo codes
 *
 * @author j.x
 * @date 2024/1/19 17:36
 * @since 0.1.0
 */
public final class DevCodes {

    private static final String GROUP = "dev";
    private static final String BO = "bo";
    private static final String PAGE = "page";

    public static final I18nCode BO_ERROR = I18nCode.of("dev.bo.error", "", GROUP, BO);
    public static final I18nCode BO_NOT_FOUND = I18nCode.of("dev.bo.not.found", "", GROUP, BO);
    public static final I18nCode BO_NONE_MATERIALIZED = I18nCode.of("dev.bo.none.materialize", "", GROUP, BO);
    public static final I18nCode BO_NONE_TABLES = I18nCode.of("dev.bo.none.tables", "", GROUP, BO);
    public static final I18nCode PAGE_NOT_FOUND = I18nCode.of("dev.page.not.found", "", GROUP, PAGE);

}
