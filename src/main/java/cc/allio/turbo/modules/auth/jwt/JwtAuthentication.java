package cc.allio.turbo.modules.auth.jwt;

import cc.allio.turbo.modules.auth.authentication.TurboJwtAuthenticationToken;
import cc.allio.turbo.modules.auth.properties.SecureProperties;
import cc.allio.turbo.modules.auth.provider.TurboUser;
import cc.allio.uno.core.util.DateUtil;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.core.util.id.IdGenerator;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.*;

import java.time.Instant;
import java.util.Date;

import static cc.allio.turbo.common.constant.Secures.*;

/**
 * jwt authentication
 *
 * @author j.x
 * @date 2024/8/28 14:12
 * @since 0.1.1
 */
public class JwtAuthentication {

    private final SecureProperties secureProperties;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public JwtAuthentication(SecureProperties secureProperties,
                             JwtEncoder jwtEncoder,
                             JwtDecoder jwtDecoder) {
        this.secureProperties = secureProperties;
        this.jwtDecoder = jwtDecoder;
        this.jwtEncoder = jwtEncoder;
    }

    /**
     * 提供{@link UserDetails}实例生成jwt token
     *
     * @param user 实例
     * @return
     */
    public TurboJwtAuthenticationToken encode(TurboUser user) {
        SecureProperties.JWT jwtProperties = secureProperties.getJwt();
        Long expireTime = jwtProperties.getExpireAt().getTime().get();
        Instant expiresAt = new Date(expireTime).toInstant();
        return encode(user, expiresAt);
    }

    /**
     * 提供{@link UserDetails}实例生成jwt token
     *
     * @param user 实例
     */
    public TurboJwtAuthenticationToken encode(TurboUser user, Instant expiresAt) {
        SecureProperties.JWT jwtProperties = secureProperties.getJwt();
        JwtClaimsSet jwtClaimsSet =
                JwtClaimsSet.builder()
                        .issuer(jwtProperties.getIssuer())
                        .issuedAt(DateUtil.now().toInstant())
                        .id(IdGenerator.defaultGenerator().toHex())
                        .subject(jwtProperties.getSubject())
                        .expiresAt(expiresAt)
                        // 加密用户信息 jws
                        .claim(ACCOUNT_NON_EXPIRED_FIELD, user.isAccountNonExpired())
                        .claim(ACCOUNT_NON_LOCKED_FIELD, user.isAccountNonLocked())
                        .claim(CREDENTIALS_NON_EXPIRED_FIELD, user.isCredentialsNonExpired())
                        .claim(ACCOUNT_ENABLED_FIELD, user.isEnabled())
                        .claim(AUTHORITIES_FIELD, user.getAuthorities())
                        .claim(USER_ID_FIELD, user.getUserId())
                        .claim(USERNAME_FIELD, user.getUsername())
                        .claim(PASSWORD_FIELD, user.getPassword())
                        .claim(ADMINISTRATOR_FIELD, user.isAdministrator())
                        .build();
        Jwt jwt = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet));
        return new TurboJwtAuthenticationToken(jwt, user);
    }

    /**
     * 解析jwt token
     *
     * @param token jwt token
     * @return 解析获取Jwt实例 maybe null if exist empty
     * @throws BadJwtException 解析失败抛出移除
     */
    public Jwt decode(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        return jwtDecoder.decode(token);
    }
}
