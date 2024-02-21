package cc.allio.turbo.modules.auth.exception;

import cc.allio.uno.core.util.IoUtils;
import cc.allio.uno.core.util.JsonUtils;
import cc.allio.turbo.common.web.R;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * oauth2 token 验证失败全局处理器
 *
 * @author j.x
 * @date 2023/10/24 13:18
 * @since 0.1.0
 */
public class Oauth2AuthenticationExceptionHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        R<Object> error = R.error(HttpStatus.UNAUTHORIZED.value(), "验证失败");
        response.setStatus(error.getCode());
        ServletOutputStream outputStream = response.getOutputStream();
        IoUtils.write(JsonUtils.toJson(error), outputStream, StandardCharsets.UTF_8);
    }
}
