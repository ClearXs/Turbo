package cc.allio.uno.turbo.common.web;

import cc.allio.uno.turbo.common.R;
import cc.allio.uno.turbo.common.exception.BizException;
import org.springframework.security.core.AuthenticationException;
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

    @ExceptionHandler(BizException.class)
    @ResponseBody
    public R handleBizException(BizException ex) {
        return R.internalError(ex);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public R handleAccessDenied(AuthenticationException ex) {
        return R.authorize(ex);
    }
}
