package cc.allio.turbo.modules.auth.provider;

import cc.allio.turbo.modules.auth.authentication.TurboJwtAuthenticationToken;
import cc.allio.turbo.modules.auth.params.*;
import cc.allio.uno.core.util.IoUtils;
import cc.allio.uno.core.util.JsonUtils;
import cc.allio.turbo.common.util.JwtUtil;
import cc.allio.turbo.common.util.WebUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;

/**
 * 参考自{@link org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider}
 * <p>执行流程为：</p>
 * <ol>
 *     <li>验证请求中验证码是否合规</li>
 *     <li>调用{@link TurboUserDetailsService#loadUserByUsername(String)}获取用户详情</li>
 *     <li>生成{@link org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken}</li>
 *     <li>创建{@link TurboJwtAuthenticationToken}</li>
 * </ol>
 *  <p>该流程基于密码模式进行生成</p>
 *
 * @author j.x
 * @date 2023/10/25 14:01
 * @since 0.1.0
 */
public class TurboJwtAuthenticationProvider extends DaoAuthenticationProvider {

    private static final String LOGIN_PATH = "/auth/login";

    public TurboJwtAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        setUserDetailsService(userDetailsService);
        setPasswordEncoder(passwordEncoder);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        HttpServletRequest request = WebUtil.getRequest();
        if (request == null) {
            throw new AuthenticationServiceException("unknown request");
        }
        boolean determined = determineLogin(request);
        if (!determined) {
            throw new AuthenticationServiceException("login request not standard, it's must be 'content-type':'application/json' 'request-method':'POST' 'url':'/auth/login'");
        }
        String loginModeString = WebUtil.getLoginMode();
        LoginMode loginMode = LoginMode.of(loginModeString);
        if (loginMode == null) {
            throw new AuthenticationServiceException("login mode is null, it must be 'EMPTY' or 'CAPTCHA_CODE' or 'VERIFICATION_CODE'");
        }
        // verify several login method valid
        UsernamePasswordAuthenticationToken newAuthentication = null;
        if (LoginMode.EMPTY == loginMode) {
            LoginUsernamePassword loginParams = getLoginParams(request, LoginUsernamePassword.class);
            newAuthentication = new UsernamePasswordAuthenticator().authenticate(loginParams);
        } else if (LoginMode.CAPTCHA_CODE == loginMode) {
            LoginCaptcha loginParams = getLoginParams(request, LoginCaptcha.class);
            newAuthentication = new CaptchaCodeAuthenticator().authenticate(loginParams);
        } else if (LoginMode.VERIFICATION_CODE == loginMode) {
            LoginVerification loginParams = getLoginParams(request, LoginVerification.class);
            newAuthentication = new VerificationCodeAuthenticator().authenticate(loginParams);
        }
        return super.authenticate(newAuthentication);
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        return JwtUtil.encode((TurboUser) user);
    }

    /**
     * 判断当前的请求是否可以进行登陆
     * <ol>
     *     <li>判断http method是否为Post</li>
     *     <li>判断{@link org.springframework.http.HttpHeaders#CONTENT_TYPE}是否为{@link org.springframework.http.MediaType#APPLICATION_JSON}</li>
     *     <li>判断请求地址是否为/auth/login</li>
     * </ol>
     *
     * @param request request
     * @return true or false
     */
    private boolean determineLogin(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        if (!LOGIN_PATH.equals(requestURI)) {
            return false;
        }
        String method = request.getMethod();
        if (!HttpMethod.POST.matches(method)) {
            return false;
        }
        String contentType = request.getHeader(HttpHeaders.CONTENT_TYPE);
        return MediaType.APPLICATION_JSON.isCompatibleWith(MediaType.parseMediaType(contentType));
    }

    /**
     * 从请求流中获取登陆参数
     *
     * @param request
     * @return
     */
    private <T extends LoginClaim> T getLoginParams(HttpServletRequest request, Class<T> loginClaimType) {
        try {
            byte[] bytes = IoUtils.readToByteArray(request.getInputStream());
            return JsonUtils.parse(bytes, loginClaimType);
        } catch (IOException e) {
            throw new AuthenticationServiceException("request login form inputStream empty");
        }
    }
}
