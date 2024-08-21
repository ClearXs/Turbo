package cc.allio.turbo.common.web;

import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.common.i18n.I18nCode;
import cc.allio.turbo.common.i18n.LocaleFormatter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
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
 * @since 0.1.0
 */
@Slf4j
@ControllerAdvice
public class ExceptionHandlerController {

    /**
     * 自定义业务异常
     */
    @ResponseBody
    @ExceptionHandler(BizException.class)
    public R handleBizException(HttpServletResponse response, BizException ex) {
        printError(ex);
        I18nCode i18nCode = ex.getI18nCode();
        String errMsg;
        if (i18nCode != null) {
            String message = LocaleFormatter.getMessage(i18nCode.getKey());
            errMsg = String.format(message, i18nCode.getParams());
        } else {
            errMsg = ex.getMessage();
        }
        R<?> r = R.internalError(errMsg);
        response.setStatus(r.getCode());
        return r;
    }

    /**
     * 认证异常处理
     */
    @ResponseBody
    @ExceptionHandler(AuthenticationException.class)
    public R handleAccessDenied(AuthenticationException ex) {
        printError(ex);
        return R.authorize(ex);
    }

    /**
     * 参数校验错误处理
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R handleNotValidateException(MethodArgumentNotValidException ex) {
        printError(ex);
        return R.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }

    /**
     * 通用异常处理
     */
    @ResponseBody
    @ExceptionHandler(Throwable.class)
    public R handleGeneralException(HttpServletResponse response, Throwable ex) {
        printError(ex);
        R<?> r = R.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        response.setStatus(r.getCode());
        return r;
    }

    private void printError(Throwable ex) {
        log.error("", ex);
    }
}
