package cc.allio.uno.turbo.auth.exception;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.IoUtil;
import cc.allio.uno.core.util.JsonUtils;
import cc.allio.uno.turbo.common.R;
import cc.allio.uno.turbo.common.i18n.ExceptionCodes;
import cc.allio.uno.turbo.common.i18n.LocaleFormatter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Authentication异常处理器
 *
 * @author j.x
 * @date 2023/10/23 17:12
 * @since 1.0.0
 */
public class AuthenticationExceptionHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String authenticationExceptionMessage = StringPool.EMPTY;
        if (authException instanceof CredentialsExpiredException) {
            authenticationExceptionMessage = LocaleFormatter.getMessage(ExceptionCodes.AUTHENTICATION_FAILED);
        } else if (authException instanceof InsufficientAuthenticationException) {
            authenticationExceptionMessage = LocaleFormatter.getMessage(ExceptionCodes.AUTHENTICATION_FAILED);
        } else if (authException instanceof CaptchaExpiredException) {
            authenticationExceptionMessage = LocaleFormatter.getMessage(ExceptionCodes.CAPTCHA_EXPIRED);
        } else if (authException instanceof CaptchaError) {
            authenticationExceptionMessage = LocaleFormatter.getMessage(ExceptionCodes.CAPTCHA_ERROR);
        }
        R<Object> error = R.error(HttpStatus.UNAUTHORIZED.value(), authenticationExceptionMessage);
        response.setStatus(error.getCode());
        ServletOutputStream outputStream = response.getOutputStream();
        IoUtil.write(JsonUtils.toJson(error), outputStream, StandardCharsets.UTF_8);
    }
}
