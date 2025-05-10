package cc.allio.turbo.modules.auth.provider;

import cc.allio.turbo.modules.auth.authority.TurboGrantedAuthority;
import cc.allio.turbo.modules.system.entity.SysUser;
import cc.allio.turbo.modules.system.enums.UserStatus;
import cc.allio.uno.core.bean.MapWrapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static cc.allio.turbo.common.constant.Secures.*;
import static cc.allio.turbo.common.constant.Secures.AVATAR_FIELD;

/**
 * auth user
 *
 * @author j.x
 * @date 2023/11/1 16:48
 * @since 0.1.0
 */
@Setter
@Getter
public class TurboUser implements UserDetails {

    private final Set<TurboGrantedAuthority> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    /**
     * user id
     */
    @Setter
    @Getter
    private String userId;

    /**
     * user name
     */
    @Setter
    private String username;

    /**
     * user password
     */
    @Setter
    private String password;

    /**
     * nickname
     */
    @Setter
    @Getter
    private String nickname;

    @Getter
    @Setter
    private String avatar;

    /**
     * whether admin
     */
    @Setter
    private boolean administrator;

    public TurboUser(String userId, String username, String password, Set<TurboGrantedAuthority> authorities) {
        this.userId = String.valueOf(userId);
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public TurboUser(JwtAuthenticationToken authentication) {
        this(authentication.getToken());
    }

    public TurboUser(SysUser user, Set<TurboGrantedAuthority> authorities) {
        UserStatus userStatus = user.getStatus();
        this.accountNonLocked = userStatus != UserStatus.LOCK;
        this.accountNonExpired = true;
        this.credentialsNonExpired = true;
        this.enabled = userStatus == UserStatus.ENABLE;
        this.userId = String.valueOf(user.getId());
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.nickname = user.getNickname();
        this.avatar = user.getAvatar();
        // determinate authority has administrator
        this.authorities = authorities;
        this.administrator = authorities.stream().anyMatch(authority -> authority.getAuthority().equals(ROLE_OF_ADMINISTRATOR));
    }

    public TurboUser(Jwt jwt) {
        this.accountNonExpired = jwt.getClaimAsBoolean(ACCOUNT_NON_EXPIRED_FIELD);
        this.accountNonLocked = jwt.getClaimAsBoolean(ACCOUNT_NON_LOCKED_FIELD);
        this.credentialsNonExpired = jwt.getClaimAsBoolean(CREDENTIALS_NON_EXPIRED_FIELD);
        this.enabled = jwt.getClaimAsBoolean(ACCOUNT_ENABLED_FIELD);
        this.userId = jwt.getClaim(USER_ID_FIELD);
        Collection<Map<String, Object>> claimAuthorities = jwt.getClaim(AUTHORITIES_FIELD);
        this.authorities =
                claimAuthorities.stream()
                        .map(auth -> {
                            MapWrapper wrapper = new MapWrapper(auth);
                            Long roleId = wrapper.getForce(ROLE_ID_FIELD, Long.class);
                            String roleCode = wrapper.getForce(ROLE_CODE_FIELD, String.class);
                            String roleName = wrapper.getForce(ROLE_NAME_FIELD, String.class);
                            return new TurboGrantedAuthority(roleId, roleCode, roleName);
                        })
                        .collect(Collectors.toSet());
        this.username = jwt.getClaimAsString(USERNAME_FIELD);
        this.password = jwt.getClaimAsString(PASSWORD_FIELD);
        this.nickname = jwt.getClaimAsString(NICKNAME_FIELD);
        this.avatar = jwt.getClaimAsString(AVATAR_FIELD);
        this.administrator = Optional.ofNullable(jwt.getClaimAsBoolean(ADMINISTRATOR_FIELD)).orElse(false);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
