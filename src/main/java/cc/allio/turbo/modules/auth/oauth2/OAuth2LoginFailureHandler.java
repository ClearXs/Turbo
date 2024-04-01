package cc.allio.turbo.modules.auth.oauth2;

import cc.allio.turbo.common.i18n.LocaleFormatter;
import cc.allio.turbo.common.web.R;
import cc.allio.uno.core.util.IoUtils;
import cc.allio.uno.core.util.JsonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static cc.allio.turbo.common.i18n.ExceptionCodes.OAUTH2_LOGIN_FAILED;

/**
 * write to failure message on {@link HttpServletResponse}
 *
 * @author j.x
 * @date 2024/3/30 17:21
 * @since 0.1.1
 */
@Slf4j
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.error("Failed to oauth2 login, url {} ", request.getRequestURI(), exception);
        // write to res
        R<?> error = R.error(HttpStatus.FORBIDDEN.value(), LocaleFormatter.getMessage(OAUTH2_LOGIN_FAILED));
        ServletOutputStream outputStream = response.getOutputStream();
        IoUtils.write(JsonUtils.toJson(error), outputStream, StandardCharsets.UTF_8);
    }
}
