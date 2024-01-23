package cc.allio.turbo.common.exception;

import cc.allio.turbo.common.i18n.I18nCode;
import lombok.Getter;

/**
 * 通用业务异常
 *
 * @author j.x
 * @date 2023/10/23 12:52
 * @since 0.1.0
 */
@Getter
public class BizException extends Exception {

    private I18nCode i18nCode;

    /**
     * 创建turbo统一异常，它将会使用i18n作为国际化的异常消息处理，如果未找到国际化的信息，则使用提供的默认消息
     *
     * @param i18nCode i18nCode
     */
    public BizException(I18nCode i18nCode) {
        this.i18nCode = i18nCode;
    }

    public BizException(String message) {
        super(message);
    }

    /**
     * 创建turbo统一异常，它将会使用i18n作为国际化的异常消息处理，如果未找到国际化的信息，则使用提供的默认消息
     *
     * @param i18nCode       i18nCode
     * @param defaultMessage defaultMessage
     */
    public BizException(I18nCode i18nCode, String defaultMessage) {
        super(defaultMessage);
        this.i18nCode = i18nCode;
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }
}
