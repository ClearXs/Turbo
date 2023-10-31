package cc.allio.uno.turbo.common;

import cc.allio.uno.core.util.StringUtils;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * 统一返回对象
 *
 * @author j.x
 * @date 2023/10/22 12:26
 * @since 1.0.0
 */
@Data
public final class R<T> {

    /**
     * 响应状态码
     *
     * @see org.springframework.http.HttpStatus
     */
    private int code;

    /**
     * 传输数据
     */
    private T data;

    /**
     * 消息
     */
    private String message;

    /**
     * success
     */
    public static <T> R<T> ok() {
        return of(HttpStatus.OK.value(), null, HttpStatus.OK.name(), null);
    }

    /**
     * success
     */
    public static <T> R<T> ok(T data) {
        return of(HttpStatus.OK.value(), data, HttpStatus.OK.name(), null);
    }

    /**
     * success
     */
    public static <T> R<T> ok(T data, String message) {
        return of(HttpStatus.OK.value(), data, message, null);
    }

    /**
     * {@link org.springframework.http.HttpStatus#UNAUTHORIZED}
     *
     * @see #of(int, Object, String, Throwable)
     */
    public static <T> R<T> authorize() {
        return of(HttpStatus.UNAUTHORIZED.value(), null, HttpStatus.UNAUTHORIZED.name(), null);
    }

    /**
     * {@link org.springframework.http.HttpStatus#UNAUTHORIZED}
     *
     * @see #of(int, Object, String, Throwable)
     */
    public static <T> R<T> authorize(Throwable ex) {
        return of(HttpStatus.UNAUTHORIZED.value(), null, HttpStatus.UNAUTHORIZED.name(), ex);
    }

    /**
     * {@link HttpStatus#INTERNAL_SERVER_ERROR}
     *
     * @see #of(int, Object, String, Throwable)
     */
    public static <T> R<T> internalError() {
        return of(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, HttpStatus.INTERNAL_SERVER_ERROR.name(), null);
    }

    /**
     * {@link HttpStatus#INTERNAL_SERVER_ERROR}
     *
     * @see #of(int, Object, String, Throwable)
     */
    public static <T> R<T> internalError(Throwable ex) {
        return of(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, HttpStatus.INTERNAL_SERVER_ERROR.name(), ex);
    }

    /**
     * 错误返回
     *
     * @see #of(int, Object, String, Throwable)
     */
    public static <T> R<T> error(int code, Throwable ex) {
        return of(code, null, null, ex);
    }

    /**
     * 错误返回
     *
     * @see #of(int, Object, String, Throwable)
     */
    public static <T> R<T> error(int code, String message) {
        return of(code, null, message, null);
    }


    /**
     * 错误返回
     *
     * @see #of(int, Object, String, Throwable)
     */
    public static <T> R<T> error(int code, String message, Throwable ex) {
        return of(code, null, message, ex);
    }

    /**
     * 示例R对象
     *
     * @param code    http 状态码
     * @param data    数据
     * @param message 消息
     * @param ex      异常
     * @param <T>     数据泛型
     * @return R
     */
    public static <T> R<T> of(int code, T data, String message, Throwable ex) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setData(data);
        if (StringUtils.isNotBlank(message)) {
            r.setMessage(message);
        } else if (ex != null) {
            r.setMessage(ex.getMessage());
        }
        return r;
    }
}
