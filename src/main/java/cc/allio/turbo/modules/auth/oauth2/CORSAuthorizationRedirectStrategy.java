package cc.allio.turbo.modules.auth.oauth2;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;

import java.io.IOException;

/**
 * wrapper to {@link DefaultRedirectStrategy} and header add Access-Control-Allow-Origin = *
 *
 * @author j.x
 * @date 2024/3/31 18:29
 * @since 0.1.1
 */
public class CORSAuthorizationRedirectStrategy implements RedirectStrategy {

    protected final RedirectStrategy internalStrategy;

    public CORSAuthorizationRedirectStrategy() {
        this.internalStrategy = new DefaultRedirectStrategy();
    }

    @Override
    public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "*");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        internalStrategy.sendRedirect(request, response, url);
    }
}
