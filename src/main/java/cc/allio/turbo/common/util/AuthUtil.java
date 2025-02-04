package cc.allio.turbo.common.util;

import cc.allio.turbo.common.constant.Secures;
import cc.allio.uno.core.bean.MapWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * auth 相关工具方法
 *
 * @author j.x
 * @date 2023/11/9 18:16
 * @since 0.1.0
 */
@Slf4j
public final class AuthUtil {

    private static final SecurityContextHolderStrategy securityContextHolderStrategy =
            SecurityContextHolder.getContextHolderStrategy();

    /**
     * 获取当前登陆用户的id
     *
     * @return Long may be null
     */
    public static Long getUserId() {
        return getAuthenticationWrapper().getUserId();
    }

    /**
     * 获取当前登陆用户的用户名
     *
     * @return maybe null
     */
    public static String getUsername() {
        return getAuthenticationWrapper().getUsername();
    }

    /**
     * get current user nickname
     *
     * @return maybe null
     */
    public static String getNickname() {
        return getAuthenticationWrapper().getNickname();
    }

    /**
     * current user whether admin
     *
     * @return admin if true
     */
    public static Boolean isAdmin() {
        return getAuthenticationWrapper().getAdministrator();
    }

    /**
     * according to {@link SecurityContext} get current {@link Authentication}
     *
     * @return the {@link Authentication} instance
     */
    public static Authentication getAuthentication() {
        return securityContextHolderStrategy.getContext().getAuthentication();
    }

    /**
     * judgment {@link Authentication}
     *
     * @return ture
     */
    public static boolean hasAuthentication() {
        return getAuthentication() != null;
    }

    /**
     * create newly {@link AuthenticationWrapper}
     *
     * @return the {@link AuthenticationWrapper} instance
     */
    public static AuthenticationWrapper getAuthenticationWrapper() {
        return new AuthenticationWrapper(getAuthentication());
    }

    /**
     * quickly grab {@link Authentication} information.
     *
     */
    public static class AuthenticationWrapper {

        private final MapWrapper wrapper;

        public AuthenticationWrapper(Authentication authentication) {
            if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
                Map<String, Object> tokenAttributes = jwtAuthenticationToken.getTokenAttributes();
                this.wrapper = new MapWrapper(tokenAttributes);
            } else {
                log.warn("current authentication is not JwtAuthenticationToken");
                this.wrapper = new MapWrapper(Collections.emptyMap());
            }
        }

        // ====================== get system internal security method ======================

        public Boolean getAccountNonExpired() {
            return wrapper.getForce(Secures.ACCOUNT_NON_EXPIRED_FIELD, Boolean.class);
        }

        public Boolean getAccountNonLocked() {
            return wrapper.getForce(Secures.ACCOUNT_NON_LOCKED_FIELD, Boolean.class);
        }

        public Boolean getCredentialsNonExpired() {
            return wrapper.getForce(Secures.CREDENTIALS_NON_EXPIRED_FIELD, Boolean.class);
        }

        public Boolean getEnabled() {
            return wrapper.getForce(Secures.ACCOUNT_ENABLED_FIELD, Boolean.class);
        }

        public Long getUserId() {
            return wrapper.getForce(Secures.USER_ID_FIELD, Long.class);
        }

        public String getUsername() {
            return wrapper.getForce(Secures.USERNAME_FIELD, String.class);
        }

        public String getNickname() {
            return wrapper.getForce(Secures.NICKNAME_FIELD, String.class);
        }

        public String getPassword() {
            return wrapper.getForce(Secures.PASSWORD_FIELD, String.class);
        }

        public Boolean getAdministrator() {
            return wrapper.getForce(Secures.ADMINISTRATOR_FIELD, Boolean.class);
        }

        public List<Map<String, Object>> getAuthorities() {
            return wrapper.getForce(Secures.AUTHORITIES_FIELD, List.class);
        }
    }
}
