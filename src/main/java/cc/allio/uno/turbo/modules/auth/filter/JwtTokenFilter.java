package cc.allio.uno.turbo.modules.auth.filter;

import cc.allio.uno.core.util.DateUtil;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.turbo.common.util.JwtUtil;
import cc.allio.uno.turbo.common.util.WebUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;

/**
 * 验证jwt token
 * <p>其流程为：</p>
 * <ol>
 *     <li>解析请求头中{@link org.springframework.http.HttpHeaders#AUTHORIZATION}是否包含，则继续往下走过滤器链路</li>
 *     <li>对头中进行验签，则继续往下走过滤器链路</li>
 *     <li>生成{@link JwtAuthenticationToken}放入SecurityContext中</li>
 * </ol>
 *
 * @author j.x
 * @date 2023/10/25 14:13
 * @see AuthenticationFilter
 * @since 1.0.0
 */
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();
    private final SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();
    private final AuthenticationFailureHandler failureHandler = new AuthenticationEntryPointFailureHandler(
            new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 1.验证请求头
            String token = request.getHeader(WebUtil.Authentication);
            if (StringUtils.isBlank(token)) {
                filterChain.doFilter(request, response);
                return;
            }
            // 2.验签
            Jwt jwt = JwtUtil.decode(token);
            if (jwt == null) {
                throw new BadCredentialsException("bad credentials");
            }
            // 3.验证是否过期
            Instant expiresAt = jwt.getExpiresAt();
            if (expiresAt != null && expiresAt.isBefore(DateUtil.now().toInstant())) {
                unsuccessfulAuthentication(request, response, new CredentialsExpiredException("token expired"));
            } else {
                AbstractAuthenticationToken newAuthentication = converter.convert(jwt);
                successfulAuthentication(request, response, filterChain, newAuthentication);
            }
        } catch (Throwable ex) {
            unsuccessfulAuthentication(request, response, new AuthenticationServiceException(ex.getMessage()));
        }
    }

    private void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            AuthenticationException failed) throws IOException, ServletException {
        this.securityContextHolderStrategy.clearContext();
        this.failureHandler.onAuthenticationFailure(request, response, failed);
    }

    private void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                          Authentication authentication) throws IOException, ServletException {
        SecurityContext context = securityContextHolderStrategy.getContext();
        if (context == null) {
            context = securityContextHolderStrategy.createEmptyContext();
        }
        context.setAuthentication(authentication);
        this.securityContextHolderStrategy.setContext(context);
        this.securityContextRepository.saveContext(context, request, response);
        chain.doFilter(request, response);
    }
}
