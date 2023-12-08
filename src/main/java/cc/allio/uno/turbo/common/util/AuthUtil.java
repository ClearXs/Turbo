package cc.allio.uno.turbo.common.util;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.turbo.modules.auth.provider.TurboUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

/**
 * auth 相关工具方法
 *
 * @author j.x
 * @date 2023/11/9 18:16
 * @since 0.1.0
 */
@Slf4j
public final class AuthUtil {

    private AuthUtil() {
    }

    /**
     * 获取当前登陆用户的id
     *
     * @return Long maybe null
     */
    public static Long getCurrentUserId() {
        return Optional.ofNullable(getCurrentUser())
                .map(TurboUser::getUserId)
                .orElse(null);
    }

    /**
     * 获取当前登陆用户的用户名
     *
     * @return maybe null
     */
    public static String getCurrentUsername() {
        return Optional.ofNullable(getCurrentUser())
                .map(TurboUser::getUsername)
                .orElse(StringPool.EMPTY);
    }

    /**
     * 获取当前用户组织id
     *
     * @return orgid or null
     */
    public static Long getCurrentUserOrgId() {
        return Optional.ofNullable(getCurrentUser())
                .map(TurboUser::getOrgId)
                .orElse(null);
    }

    /**
     * 获取当前用户租户id
     *
     * @return tenant id or null
     */
    public static Long getCurrentTenantId() {
        return Optional.ofNullable(getCurrentUser())
                .map(TurboUser::getTenantId)
                .orElse(null);
    }

    /**
     * 获取当前登陆的用户
     *
     * @return TurboUser or null
     */
    public static TurboUser getCurrentUser() {
        try {
            Jwt jwt = JwtUtil.decode(WebUtil.getToken());
            return Optional.ofNullable(jwt)
                    .map(TurboUser::new)
                    .orElse(null);
        } catch (BadJwtException ex) {
            log.debug("decode jwt token error", ex);
        }
        return null;
    }
}
