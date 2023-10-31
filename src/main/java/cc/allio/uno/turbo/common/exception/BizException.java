package cc.allio.uno.turbo.common.exception;

/**
 * 通用业务异常
 *
 * @author j.x
 * @date 2023/10/23 12:52
 * @since 1.0.0
 */
public class BizException extends Exception {

    public BizException() {
    }

    public BizException(String message) {
        super(message);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizException(Throwable cause) {
        super(cause);
    }

    public BizException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
