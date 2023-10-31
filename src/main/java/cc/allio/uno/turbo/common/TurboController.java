package cc.allio.uno.turbo.common;

import org.springframework.http.HttpStatus;

/**
 * 封装controller层好用的方法
 *
 * @author j.x
 * @date 2023/10/22 14:52
 * @since 1.0.0
 */
public abstract class TurboController  {

    /**
     * success
     */
    public <T> R<T> ok() {
        return R.of(HttpStatus.OK.value(), null, HttpStatus.OK.name(), null);
    }

    /**
     * success
     */
    public <T> R<T> ok(T data) {
        return R.of(HttpStatus.OK.value(), data, HttpStatus.OK.name(), null);
    }

    /**
     * success
     */
    public <T> R<T> ok(T data, String message) {
        return R.of(HttpStatus.OK.value(), data, message, null);
    }

    /**
     * {@link org.springframework.http.HttpStatus#UNAUTHORIZED}
     */
    public <T> R<T> authorize() {
        return R.of(HttpStatus.UNAUTHORIZED.value(), null, HttpStatus.UNAUTHORIZED.name(), null);
    }

    /**
     * {@link org.springframework.http.HttpStatus#UNAUTHORIZED}
     */
    public <T> R<T> authorize(Throwable ex) {
        return R.of(HttpStatus.UNAUTHORIZED.value(), null, HttpStatus.UNAUTHORIZED.name(), ex);
    }

    /**
     * {@link HttpStatus#INTERNAL_SERVER_ERROR}
     */
    public <T> R<T> internalError() {
        return R.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, HttpStatus.INTERNAL_SERVER_ERROR.name(), null);
    }

    /**
     * {@link HttpStatus#INTERNAL_SERVER_ERROR}
     */
    public <T> R<T> internalError(Throwable ex) {
        return R.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, HttpStatus.INTERNAL_SERVER_ERROR.name(), ex);
    }

    /**
     * 错误返回
     */
    public <T> R<T> error(int code, Throwable ex) {
        return R.of(code, null, null, ex);
    }

    /**
     * 错误返回
     */
    public <T> R<T> error(int code, String message, Throwable ex) {
        return R.of(code, null, message, ex);
    }

}
