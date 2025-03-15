package cc.allio.turbo.modules.auth.filter;

import cc.allio.turbo.common.util.WebUtil;
import cc.allio.turbo.modules.auth.authentication.TurboJwtAuthenticationToken;
import cc.allio.turbo.modules.auth.jwt.JwtAuthentication;
import cc.allio.turbo.modules.auth.properties.SecureProperties;
import cc.allio.turbo.modules.auth.provider.TurboUser;
import cc.allio.uno.core.util.JsonUtils;
import com.google.common.collect.Sets;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

/**
 * if request header contains {@link WebUtil#X_CIPHER} and {@link SecureProperties#getCipher()} consistence.
 * then will be parsed to {@link WebUtil#X_CIPHER_USER} and set {@link Authentication} to {@link SecurityContext}
 *
 * @author j.x
 * @since 0.2.0
 */
@Slf4j
public class CipherFilter extends OncePerRequestFilter {

    private final SecureProperties secureProperties;
    private final JwtAuthentication jwtAuthentication;
    private final JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();
    private final SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();

    public CipherFilter(SecureProperties secureProperties, JwtAuthentication jwtAuthentication) {
        this.secureProperties = secureProperties;
        this.jwtAuthentication = jwtAuthentication;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Optional<String> cipherOpt = WebUtil.getHeaderOpt(WebUtil.X_CIPHER);

        if (cipherOpt.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        String cipher = cipherOpt.get();

        if (!secureProperties.getCipher().equals(cipher)) {
            if (log.isDebugEnabled()) {
                log.debug("cipher not match, cipher: {}", cipher);
            }
            filterChain.doFilter(request, response);
            return;
        }

        Optional<String> cipherUserOpt = WebUtil.getHeaderOpt(WebUtil.X_CIPHER_USER);

        cipherUserOpt.flatMap(cipherUserString -> {
                    CipherUser cipherUser;
                    try {
                        cipherUser = JsonUtils.parse(cipherUserString, CipherUser.class);
                    } catch (Exception e) {
                        if (log.isDebugEnabled()) {
                            log.debug("cipher user parse error, cipher user: {}", cipherUserString);
                        }

                        return Optional.empty();
                    }
                    return Optional.of(cipherUser);
                })
                .ifPresent(cipherUser -> {
                    TurboJwtAuthenticationToken authenticationToken = buildCipherAuthenticationToken(cipherUser);
                    Jwt token = authenticationToken.getToken();
                    Authentication authentication = converter.convert(token);
                    SecurityContext context = securityContextHolderStrategy.getContext();
                    if (context == null) {
                        context = securityContextHolderStrategy.createEmptyContext();
                    }
                    context.setAuthentication(authentication);
                    this.securityContextHolderStrategy.setContext(context);
                    this.securityContextRepository.saveContext(context, request, response);

                    // set token to attributes
                    String tokenValue = token.getTokenValue();
                    request.setAttribute(WebUtil.X_AUTHENTICATION, tokenValue);
                });

        filterChain.doFilter(request, response);
    }

    public TurboJwtAuthenticationToken buildCipherAuthenticationToken(CipherUser cipherUser) {
        Instant expired = new Date().toInstant().plus(10000, ChronoUnit.DAYS);

        String userId = cipherUser.getUserId();
        String username = cipherUser.getUsername();
        String nickname = cipherUser.getNickname();
        String avatar = cipherUser.getAvatar();
        TurboUser turboUser = new TurboUser(userId, username, username, Sets.newHashSet());
        turboUser.setAvatar(avatar);
        turboUser.setNickname(nickname);

        return jwtAuthentication.encode(turboUser, expired);
    }

    @Data
    public static class CipherUser {
        String userId;
        String username;
        String nickname;
        String avatar;
    }
}
