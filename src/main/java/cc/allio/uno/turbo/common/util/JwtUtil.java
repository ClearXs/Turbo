package cc.allio.uno.turbo.common.util;

import cc.allio.uno.core.bean.ObjectWrapper;
import cc.allio.uno.core.util.DateUtil;
import cc.allio.uno.core.util.id.IdGenerator;
import cc.allio.uno.turbo.auth.SecureProperties;
import cc.allio.uno.turbo.auth.authentication.TurboJwtAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.*;

import java.time.Instant;
import java.util.Date;

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
    public static TurboJwtAuthenticationToken encode(UserDetails userDetails) {
        Long expireTime = getInstance().secureProperties.getJwt().getExpireAt().getTime().get();
        Instant expiresAt = new Date(expireTime).toInstant();
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuer(getInstance().secureProperties.getJwt().getIssuer())
                .issuedAt(DateUtil.now().toInstant())
                .id(IdGenerator.defaultGenerator().toHex())
                .subject(getInstance().secureProperties.getJwt().getSubject())
                .expiresAt(expiresAt)
                // 加密用户信息 jws
                .claims((claims) -> claims.putAll(new ObjectWrapper(userDetails).findAllValuesForce()))
                .build();
        Jwt jwt = getInstance().jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet));
        return new TurboJwtAuthenticationToken(jwt, userDetails);
    }

    /**
     * 解析jwt token
     *
     * @param token jwt token
     * @return 解析获取Jwt实例
     */
    public static Jwt decode(String token) {
        return getInstance().jwtDecoder.decode(token);
    }

    private static JwtUtil getInstance() {
        return instance;
    }
}
