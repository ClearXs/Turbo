package cc.allio.turbo.modules.auth.web;

import cc.allio.turbo.common.util.WebUtil;
import cc.allio.turbo.modules.auth.jwt.JwtAuthentication;
import cc.allio.turbo.modules.auth.provider.TurboUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.data.util.Optionals;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

/**
 * resolve the {@link TurboUser}.
 * <p></p>
 *
 * @author j.x
 * @date 2024/8/27 10:43
 * @since 0.1.1
 */
@Slf4j
public class TurboUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtAuthentication jwtAuthentication;
    private static final SecurityContextHolderStrategy securityContextHolderStrategy =
            SecurityContextHolder.getContextHolderStrategy();

    public TurboUserArgumentResolver(JwtAuthentication jwtAuthentication) {
        this.jwtAuthentication = jwtAuthentication;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(TurboUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // try to request get user and then from token
        return Optionals.firstNonEmpty(
                        () -> loadByAuthentication(webRequest)
                )
                .orElse(null);
    }

    /**
     * from current request header or parameter
     *
     * @param webRequest the current request
     * @return
     */
    Optional<TurboUser> loadByAuthentication(NativeWebRequest webRequest) {
        SecurityContext context = securityContextHolderStrategy.getContext();
        Authentication authentication = context.getAuthentication();
        return Optional.ofNullable(authentication)
                .map(JwtAuthenticationToken.class::cast)
                .map(TurboUser::new)
                .or(() ->
                        Optionals.firstNonEmpty(
                                        // first try to security context holder
                                        () -> Optional.ofNullable(webRequest.getHeader(WebUtil.X_AUTHENTICATION)),
                                        () -> Optional.ofNullable(webRequest.getParameter(WebUtil.X_AUTHENTICATION))
                                )
                                .flatMap(token -> {
                                    Jwt jwt = jwtAuthentication.decode(token);
                                    return Optional.ofNullable(jwt).map(TurboUser::new);
                                }));
    }
}
