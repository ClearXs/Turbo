package cc.allio.uno.turbo.common.util;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.turbo.auth.provider.TurboUser;

import java.util.Optional;

/**
 * auth 相关工具方法
 *
 * @author j.x
 * @date 2023/11/9 18:16
 * @since 1.0.0
 */
public class AuthUtil {

    /**
     * 获取当前登陆用户的id
     *
     * @return Long
     */
    public static Long getCurrentUserId() {
        return Optional.ofNullable(getCurrentUser())
                .map(TurboUser::getUserId)
                .orElse(null);
    }

    /**
     * 获取当前登陆用户的用户名
     *
     * @return
     */
    public static String getCurrentUsername() {
        return Optional.ofNullable(getCurrentUser())
                .map(TurboUser::getUsername)
                .orElse(StringPool.EMPTY);
    }

    /**
     * 获取当前登陆的用户
     *
     * @return TurboUser or null
     */
    public static TurboUser getCurrentUser() {
        return Optional.ofNullable(JwtUtil.decode(WebUtil.getToken()))
                .map(TurboUser::new)
                .orElse(null);
    }
}
