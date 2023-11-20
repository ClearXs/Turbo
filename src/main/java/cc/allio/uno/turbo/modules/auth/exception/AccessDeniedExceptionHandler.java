package cc.allio.uno.turbo.modules.auth.exception;

import cc.allio.uno.core.util.IoUtil;
import cc.allio.uno.core.util.JsonUtils;
import cc.allio.uno.turbo.common.web.R;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class AccessDeniedExceptionHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        R<?> error = R.error(HttpStatus.FORBIDDEN.value(), "用户没有权限");
        ServletOutputStream outputStream = response.getOutputStream();
        IoUtil.write(JsonUtils.toJson(error), outputStream, StandardCharsets.UTF_8);
    }
}
