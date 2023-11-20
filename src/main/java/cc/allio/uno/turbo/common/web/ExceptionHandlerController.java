package cc.allio.uno.turbo.common.web;

import cc.allio.uno.turbo.common.exception.BizException;
import cc.allio.uno.turbo.common.i18n.LocaleFormatter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 处理全局异常问题
 *
 * @author j.x
 * @date 2023/10/23 13:47
 * @since 1.0.0
 */
@ControllerAdvice
public class ExceptionHandlerController {

    /**
     * 自定义业务异常
     */
    @ResponseBody
    @ExceptionHandler(BizException.class)
    public R handleBizException(BizException ex) {
        String i18nCode = ex.getI18nCode();
        String errMsg = LocaleFormatter.getMessage(i18nCode);
        return R.internalError(errMsg);
    }

    /**
     * 认证异常处理
     */
    @ResponseBody
    @ExceptionHandler(AuthenticationException.class)
    public R handleAccessDenied(AuthenticationException ex) {
        return R.authorize(ex);
    }

    /**
     * 参数校验错误处理
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R handleNotValidateException(MethodArgumentNotValidException ex) {
        return R.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }

    /**
     * 通用异常处理
     */
    @ResponseBody
    @ExceptionHandler(Throwable.class)
    public R handleGeneralException(Throwable ex) {
        return R.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }
}
