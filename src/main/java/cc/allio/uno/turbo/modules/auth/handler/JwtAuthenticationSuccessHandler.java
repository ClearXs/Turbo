package cc.allio.uno.turbo.modules.auth.handler;

import cc.allio.uno.core.util.IoUtil;
import cc.allio.uno.core.util.JsonUtils;
import cc.allio.uno.turbo.modules.auth.authentication.TurboJwtAuthenticationToken;
import cc.allio.uno.turbo.common.web.R;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 通过JWT验证成功后，数据传递给调用者
 *
 * @author j.x
 * @date 2023/10/26 17:46
 * @since 1.0.0
 */
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication instanceof TurboJwtAuthenticationToken) {
            Jwt token = ((TurboJwtAuthenticationToken) authentication).getToken();
            R<Jwt> ok = R.ok(token);
            response.setStatus(ok.getCode());
            IoUtil.write(JsonUtils.toJson(ok), response.getOutputStream(), StandardCharsets.UTF_8);
        }
    }
}
