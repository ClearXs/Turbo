package cc.allio.turbo.modules.auth.exception;

import cc.allio.uno.core.util.IoUtils;
import cc.allio.uno.core.util.JsonUtils;
import cc.allio.turbo.common.web.R;
import cc.allio.turbo.common.i18n.ExceptionCodes;
import cc.allio.turbo.common.i18n.LocaleFormatter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Authentication异常处理器
 *
 * @author j.x
 * @date 2023/10/23 17:12
 * @since 0.1.0
 */
public class AuthenticationExceptionHandler implements AuthenticationEntryPoint, AuthenticationFailureHandler {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        doHandleAuthenticationException(request, response, authException);
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        doHandleAuthenticationException(request, response, exception);
    }

    private void doHandleAuthenticationException(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        // 获取i18n错误信息
        String authenticationExceptionMessage = switch (authException) {
            case CredentialsExpiredException credentialsExpiredException ->
                    LocaleFormatter.getMessage(ExceptionCodes.AUTHENTICATION_FAILED.getKey());
            case InsufficientAuthenticationException insufficientAuthenticationException ->
                    LocaleFormatter.getMessage(ExceptionCodes.AUTHENTICATION_FAILED.getKey());
            case CaptchaExpiredException captchaExpiredException ->
                    LocaleFormatter.getMessage(ExceptionCodes.CAPTCHA_EXPIRED.getKey());
            case CaptchaError captchaError -> LocaleFormatter.getMessage(ExceptionCodes.CAPTCHA_ERROR.getKey());
            case null, default -> authException.getMessage();
        };
        R<Object> error = R.error(HttpStatus.UNAUTHORIZED.value(), authenticationExceptionMessage);
        response.setStatus(error.getCode());
        ServletOutputStream outputStream = response.getOutputStream();
        IoUtils.write(JsonUtils.toJson(error), outputStream, StandardCharsets.UTF_8);
    }
}
