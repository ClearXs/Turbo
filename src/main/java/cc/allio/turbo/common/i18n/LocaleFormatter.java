package cc.allio.turbo.common.i18n;

import cc.allio.turbo.common.util.WebUtil;
import cc.allio.uno.core.StringPool;
import org.springframework.beans.BeansException;
import org.springframework.context.*;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;
import java.util.Optional;


/**
 * 国际化常用的工具类
 *
 * @author j.x
 * @date 2023/10/27 17:46
 * @since 0.1.0
 */
public class LocaleFormatter implements ApplicationContextAware {

    /**
     * 基于spring
     */
    private static MessageSource messageSource;

    /**
     * 当前语言环境，使用volatile避免使用锁，默认为中文环境
     */
    public static volatile Locale locale = Locale.CHINA;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        LocaleFormatter.messageSource = applicationContext.getBean(ResourceBundleMessageSource.class);
    }

    /**
     * @see #getMessage(String, Object[], String)
     */
    public static String getMessage(String code) {
        return getMessage(code, new Object[]{}, StringPool.EMPTY);
    }

    /**
     * @see #getMessage(String, Object[], String)
     */
    public static String getMessage(String code, Object[] args) {
        return getMessage(code, args, StringPool.EMPTY);
    }

    /**
     * @see #getMessage(String, Object[], String)
     */
    public static String getMessage(I18nCode code) {
        return getMessage(code.getKey(), new Object[]{}, StringPool.EMPTY);
    }

    /**
     * @see #getMessage(String, Object[], String)
     */
    public static String getMessage(I18nCode code, Object[] args) {
        return getMessage(code.getKey(), args, StringPool.EMPTY);
    }

    /**
     * @see #getMessage(String, Object[], String)
     */
    public static String getMessage(I18nCode code, Object[] args, String defaultMessage) {
        return getMessage(code.getKey(), args, defaultMessage);
    }

    /**
     * 尝试从给定的code中获取国际化的信息，使用{@link Locale}
     *
     * @param code           在properties文件中声明的标识
     * @param args           数据占位符
     * @param defaultMessage 如果为空默认的消息
     * @return 国际化信息
     */
    public static String getMessage(String code, Object[] args, String defaultMessage) {
        Optional.ofNullable(WebUtil.getRequest()).ifPresent(request -> setLocale(request.getLocale()));
        return messageSource.getMessage(code, args, defaultMessage, locale);
    }

    /**
     * 尝试从给定的code中获取国际化的信息，使用{@link Locale}
     *
     * @param resolvable MessageSourceResolvable instance
     * @return 国际化信息
     */
    public static String getMessage(MessageSourceResolvable resolvable) {
        return messageSource.getMessage(resolvable, locale);
    }

    /**
     * 设置当前的语言环境
     *
     * @param locale locale
     */
    public static void setLocale(Locale locale) {
        LocaleFormatter.locale = locale;
    }
}
