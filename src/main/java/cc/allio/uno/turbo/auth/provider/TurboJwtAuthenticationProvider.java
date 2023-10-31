package cc.allio.uno.turbo.auth.provider;

import cc.allio.uno.core.util.DateUtil;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.core.util.id.IdGenerator;
import cc.allio.uno.turbo.auth.SecureProperties;
import cc.allio.uno.turbo.auth.authentication.TurboJwtAuthenticationToken;
import cc.allio.uno.turbo.auth.exception.CaptchaError;
import cc.allio.uno.turbo.auth.exception.CaptchaExpiredException;
import cc.allio.uno.turbo.common.cache.Caches;
import cc.allio.uno.turbo.common.cache.TurboCache;
import cc.allio.uno.turbo.common.util.WebUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;
import java.util.Date;

/**
 * 参考自{@link org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider}
 * <p>执行流程为：</p>
 * <ol>
 *     <li>验证请求中验证码是否合规</li>
 *     <li>调用{@link TurboUserServiceDetails#loadUserByUsername(String)}获取用户详情</li>
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

    private final JwtEncoder jwtEncoder;
    private final SecureProperties secureProperties;

    public TurboJwtAuthenticationProvider(UserDetailsService userDetailsService,
                                          PasswordEncoder passwordEncoder,
                                          JwtEncoder jwtEncoder,
                                          SecureProperties secureProperties) {
        setUserDetailsService(userDetailsService);
        setPasswordEncoder(passwordEncoder);
        this.jwtEncoder = jwtEncoder;
        this.secureProperties = secureProperties;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 验证验证码
        HttpServletRequest request = WebUtil.getRequest();
        TurboCache cache = Caches.getIfAbsent(Caches.CAPTCHA);
        String captchaId = request.getParameter("captchaId");
        // 判断是否存在
        if (StringUtils.isBlank(captchaId) || !cache.hasKey(captchaId)) {
            throw new CaptchaExpiredException();
        }
        String cacheContent = cache.get(captchaId, String.class);
        // 验证码内容
        String captcha = request.getParameter("captcha");
        // 获取大小写，验证验证码内容
        if (!captcha.equalsIgnoreCase(cacheContent)) {
            throw new CaptchaError();
        }
        return super.authenticate(authentication);
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        // 1.生成Jwt对象
        Long expireTime = secureProperties.getJwt().getExpireAt().getTime().get();
        Instant expiresAt = new Date(expireTime).toInstant();
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuer(secureProperties.getJwt().getIssuer())
                .issuedAt(DateUtil.now().toInstant())
                .id(IdGenerator.defaultGenerator().toHex())
                .subject(secureProperties.getJwt().getSubject())
                .expiresAt(expiresAt)
                // 加密用户信息 jws
                .claim("username", user.getUsername())
                .build();
        Jwt jwt = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet));
        // 2.生成JwtAuthenticationToken
        return new TurboJwtAuthenticationToken(jwt, user);
    }
}
