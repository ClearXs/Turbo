package cc.allio.uno.turbo.common.util;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.DateUtil;
import cc.allio.uno.core.util.JsonUtils;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.core.util.id.IdGenerator;
import cc.allio.uno.turbo.modules.auth.properties.SecureProperties;
import cc.allio.uno.turbo.modules.auth.authentication.TurboJwtAuthenticationToken;
import cc.allio.uno.turbo.modules.auth.provider.TurboUser;
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
 * @since 1.0.0
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
     * @param userDetails 实例
     * @return
     */
    public static synchronized TurboJwtAuthenticationToken encode(TurboUser user) {
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
                .claim("profile", JsonUtils.toJson(user))
                .claim("accountNonExpired", user.isAccountNonExpired())
                .claim("accountNonLocked", user.isAccountNonLocked())
                .claim("credentialsNonExpired", user.isCredentialsNonExpired())
                .claim("enabled", user.isEnabled())
                .claim("authorities", user.getAuthorities())
                .claim("userId", user.getUserId())
                .claim("username", user.getUsername())
                .claim("password", user.getPassword())
                .claim("email", Optional.ofNullable(user.getEmail()).orElse(StringPool.EMPTY))
                .claim("phone", Optional.ofNullable(user.getPhone()).orElse(StringPool.EMPTY))
                .claim("avatar", Optional.ofNullable(user.getAvatar()).orElse(StringPool.EMPTY))
                .claim("nickname", Optional.ofNullable(user.getNickname()).orElse(StringPool.EMPTY))
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
