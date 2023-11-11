package cc.allio.uno.turbo.auth.provider;

import cc.allio.uno.core.util.IoUtil;
import cc.allio.uno.core.util.JsonUtils;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.turbo.auth.authentication.TurboJwtAuthenticationToken;
import cc.allio.uno.turbo.auth.exception.CaptchaError;
import cc.allio.uno.turbo.auth.exception.CaptchaExpiredException;
import cc.allio.uno.turbo.auth.params.LoginParams;
import cc.allio.uno.turbo.common.util.JwtUtil;
import cc.allio.uno.turbo.common.cache.Caches;
import cc.allio.uno.turbo.common.cache.TurboCache;
import cc.allio.uno.turbo.common.util.WebUtil;
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
 * @since 1.0.0
 */
public class TurboJwtAuthenticationProvider extends DaoAuthenticationProvider {

    public static final String LOGIN_PATH = "/auth/login";

    public TurboJwtAuthenticationProvider(UserDetailsService userDetailsService,
                                          PasswordEncoder passwordEncoder) {
        setUserDetailsService(userDetailsService);
        setPasswordEncoder(passwordEncoder);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 1.验证登陆请求
        HttpServletRequest request = WebUtil.getRequest();
        boolean determined = determineLogin(request);
        if (!determined) {
            throw new AuthenticationServiceException("login request not standard, it's must be 'content-type':'application/json' 'request-method':'POST' 'url':'/auth/login'");
        }
        // 2.验证验证码
        TurboCache cache = Caches.getIfAbsent(Caches.CAPTCHA);
        LoginParams loginParams = getLoginParams(request);
        String captchaId = loginParams.getCaptchaId();
        // 判断是否存在
        if (StringUtils.isBlank(captchaId) || !cache.hasKey(captchaId)) {
            throw new CaptchaExpiredException();
        }
        String cacheContent = cache.get(captchaId, String.class);
        // 验证码内容
        String captcha = loginParams.getCaptcha();
        // 获取大小写，验证验证码内容
        if (!captcha.equalsIgnoreCase(cacheContent)) {
            throw new CaptchaError();
        }
        // 3.更改Authentication 替换为登陆信息
        UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(loginParams.getUsername(), loginParams.getPassword());
        return super.authenticate(newAuthentication);
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        return JwtUtil.encode(user);
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
    private LoginParams getLoginParams(HttpServletRequest request) {
        try {
            byte[] bytes = IoUtil.readToByteArray(request.getInputStream());
            return JsonUtils.parse(bytes, LoginParams.class);
        } catch (IOException e) {
            throw new AuthenticationServiceException("request login form is empty");
        }
    }
}
