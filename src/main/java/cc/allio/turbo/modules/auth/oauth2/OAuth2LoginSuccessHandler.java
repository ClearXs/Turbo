package cc.allio.turbo.modules.auth.oauth2;

import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.common.i18n.LocaleFormatter;
import cc.allio.turbo.common.util.WebUtil;
import cc.allio.turbo.common.web.R;
import cc.allio.uno.core.util.IoUtils;
import cc.allio.uno.core.util.JsonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static cc.allio.turbo.common.i18n.ExceptionCodes.MISTAKE_OAUTH2_TOKEN;

/**
 * TODO
 *
 * @author j.x
 * @date 2024/3/30 17:23
 * @since 0.1.1
 */
@Slf4j
@AllArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final OAuth2TokenGenerator oAuth2TokenGenerator;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication instanceof OAuth2AuthenticationToken oAuth2AuthenticationToken) {
            try {
                Jwt token = oAuth2TokenGenerator.createToken(oAuth2AuthenticationToken);
                if (log.isInfoEnabled()) {
                    log.info("Oauth2 login success, create token is {}", token.getClaims());
                }
                String tokenValue = token.getTokenValue();
                Cookie cookie = new Cookie(WebUtil.X_AUTHENTICATION, tokenValue);
                response.addCookie(cookie);
                // redirect
                response.sendRedirect("/home");
            } catch (BizException ex) {
                throw new IOException(ex.getMessage());
            }
        } else {
            // write to error
            R<?> error = R.error(HttpStatus.FORBIDDEN.value(), LocaleFormatter.getMessage(MISTAKE_OAUTH2_TOKEN));
            ServletOutputStream outputStream = response.getOutputStream();
            IoUtils.write(JsonUtils.toJson(error), outputStream, StandardCharsets.UTF_8);
        }
    }

}
