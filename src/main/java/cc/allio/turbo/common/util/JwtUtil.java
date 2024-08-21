package cc.allio.turbo.common.util;

import cc.allio.turbo.modules.auth.authentication.TurboJwtAuthenticationToken;
import cc.allio.turbo.modules.auth.properties.SecureProperties;
import cc.allio.turbo.modules.auth.provider.TurboUser;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.DateUtil;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.core.util.id.IdGenerator;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.*;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

/**
 * jwt工具
 *
 * @author j.x
 * @date 2023/11/1 16:23
 * @since 0.1.0
 */
public final class JwtUtil {

    JwtEncoder jwtEncoder;
    JwtDecoder jwtDecoder;
    SecureProperties secureProperties;
    public static JwtUtil instance;

    public JwtUtil(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder, SecureProperties secureProperties) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.secureProperties = secureProperties;
    }

    /**
     * 提供{@link UserDetails}实例生成jwt token
     *
     * @param user 实例
     * @return
     */
    public static TurboJwtAuthenticationToken encode(TurboUser user) {
        JwtUtil instance = getInstance();
        Long expireTime = instance.secureProperties.getJwt().getExpireAt().getTime().get();
        Instant expiresAt = new Date(expireTime).toInstant();
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuer(instance.secureProperties.getJwt().getIssuer())
                .issuedAt(DateUtil.now().toInstant())
                .id(IdGenerator.defaultGenerator().toHex())
                .subject(instance.secureProperties.getJwt().getSubject())
                .expiresAt(expiresAt)
                // 加密用户信息 jws
                .claim(TurboUser.ACCOUNT_NON_EXPIRED_FIELD, user.isAccountNonExpired())
                .claim(TurboUser.ACCOUNT_NON_LOCKED_FIELD, user.isAccountNonLocked())
                .claim(TurboUser.CREDENTIALS_NON_EXPIRED_FIELD, user.isCredentialsNonExpired())
                .claim(TurboUser.ACCOUNT_ENABLED_FIELD, user.isEnabled())
                .claim(TurboUser.AUTHORITIES_FIELD, user.getAuthorities())
                .claim(TurboUser.USER_ID_FIELD, user.getUserId())
                .claim(TurboUser.USERNAME_FIELD, user.getUsername())
                .claim(TurboUser.PASSWORD_FIELD, user.getPassword())
                .claim(TurboUser.EMAIL_FIELD, Optional.ofNullable(user.getEmail()).orElse(StringPool.EMPTY))
                .claim(TurboUser.PHONE_FIELD, Optional.ofNullable(user.getPhone()).orElse(StringPool.EMPTY))
                .claim(TurboUser.AVATAR_FIELD, Optional.ofNullable(user.getAvatar()).orElse(StringPool.EMPTY))
                .claim(TurboUser.NICKNAME_FIELD, Optional.ofNullable(user.getNickname()).orElse(StringPool.EMPTY))
                .claim(TurboUser.TENANT_ID_FIELD, Optional.ofNullable(user.getTenantId()).orElse(0L))
                .claim(TurboUser.ADMINISTRATOR_FIELD, user.isAdministrator())
                .build();
        Jwt jwt = instance.jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet));
        return new TurboJwtAuthenticationToken(jwt, user);
    }

    /**
     * 解析jwt token
     *
     * @param token jwt token
     * @return 解析获取Jwt实例 maybe null if exist empty
     * @throws BadJwtException 解析失败抛出移除
     */
    public static Jwt decode(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        return getInstance().jwtDecoder.decode(token);
    }

    private static JwtUtil getInstance() {
        return instance;
    }
}
