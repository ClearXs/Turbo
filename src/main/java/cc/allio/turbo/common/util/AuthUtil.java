package cc.allio.turbo.common.util;

import cc.allio.uno.core.StringPool;
import cc.allio.turbo.modules.auth.provider.TurboUser;
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
        return Optional.ofNullable(getUser())
                .map(TurboUser::getUserId)
                .orElse(null);
    }

    /**
     * 获取当前登陆用户的用户名
     *
     * @return maybe null
     */
    public static String getUsername() {
        return Optional.ofNullable(getUser())
                .map(TurboUser::getUsername)
                .orElse(StringPool.EMPTY);
    }

    /**
     * 获取当前用户组织id
     *
     * @return orgid or null
     */
    public static Long getUserOrgId() {
        return Optional.ofNullable(getUser())
                .map(TurboUser::getOrgId)
                .orElse(null);
    }

    /**
     * 获取当前用户租户id
     *
     * @return tenant id or null
     */
    public static Long getTenantId() {
        return Optional.ofNullable(getUser())
                .map(TurboUser::getTenantId)
                .orElse(null);
    }

    /**
     * 获取当前登陆的用户
     *
     * @return TurboUser or null
     */
    public static TurboUser getUser() {
        try {
            Jwt jwt = JwtUtil.decode(WebUtil.getToken());
            return Optional.ofNullable(jwt).map(TurboUser::new).orElse(null);
        } catch (BadJwtException ex) {
            log.error("decode jwt token error", ex);
        }
        return null;
    }

    /**
     * current user whether admin
     *
     * @return admin if true
     */
    public static Boolean isAdmin() {
        return Optional.ofNullable(getUser()).map(TurboUser::getIsAdmin).orElse(false);
    }
}
