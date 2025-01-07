package cc.allio.turbo.modules.auth.filter;

import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.common.util.WebUtil;
import cc.allio.turbo.modules.auth.oauth2.OAuth2Provider;
import cc.allio.turbo.modules.auth.oauth2.OAuth2TokenGenerator;
import cc.allio.uno.core.util.JsonUtils;
import cc.allio.uno.core.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;

/**
 * have the aid of oauth2 concept allow request deliver contains 'X-USER' header, and wrapper to 'X-USER' for system user
 * create new {@link JwtAuthenticationToken}, utilize {@link OAuth2TokenGenerator} create new jwt token
 *
 * @author j.x
 * @date 2024/8/29 14:20
 * @since 0.1.1
 */
@Slf4j
public class InternalOAuth2AuthenticationFilter extends OncePerRequestFilter {

    private final OAuth2TokenGenerator tokenGenerator;

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();
    private final JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

    public InternalOAuth2AuthenticationFilter(OAuth2TokenGenerator tokenGenerator) {
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // load request header
        OAuth2AuthenticationToken oAuth2AuthenticationToken = tryBestParse();
        if (oAuth2AuthenticationToken != null) {
            Jwt jwt;
            try {
                jwt = tokenGenerator.createToken(oAuth2AuthenticationToken);
            } catch (BizException e) {
                throw new ServletException(e);
            }

            if (jwt != null) {
                // set authentication to security context
                Authentication authentication = converter.convert(jwt);
                SecurityContext context = securityContextHolderStrategy.getContext();
                if (context == null) {
                    context = securityContextHolderStrategy.createEmptyContext();
                }
                context.setAuthentication(authentication);
                this.securityContextHolderStrategy.setContext(context);
            }
        }

        // jump to next one filter
        filterChain.doFilter(request, response);
    }

    // try best to parse request header or parameter use info
    OAuth2AuthenticationToken tryBestParse() {
        String userIdentifier = WebUtil.getUserIdentifier();
        if (StringUtils.isBlank(userIdentifier)) {
            return null;
        }
        // try best to parse request header or parameter use info
        Map<String, Object> user;

        // try to base64 parse
        byte[] decodeUserString = Base64.getDecoder().decode(userIdentifier);
        try {
            user = JsonUtils.toMap(new String(decodeUserString));
        } catch (Exception ex) {
            log.error("Failed parse from web parameter get TurboUser", ex);
            return null;
        }
        if (user == null) {
            try {
                user = JsonUtils.toMap(userIdentifier);
            } catch (Exception ex) {
                log.error("Failed parse from web parameter get TurboUser", ex);
                return null;
            }
        }

        String registrationId = OAuth2Provider.CUSTOM.getRegistrationId();
        user.put(registrationId, registrationId);
        DefaultOAuth2User oAuth2User = new DefaultOAuth2User(Collections.emptyList(), user, registrationId);
        return new OAuth2AuthenticationToken(oAuth2User, Collections.emptyList(), registrationId);
    }
}
