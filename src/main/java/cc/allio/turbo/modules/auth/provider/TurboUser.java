package cc.allio.turbo.modules.auth.provider;

import cc.allio.turbo.modules.auth.authority.TurboGrantedAuthority;
import cc.allio.turbo.modules.system.constant.UserStatus;
import cc.allio.turbo.modules.system.domain.SysUserVO;
import cc.allio.uno.core.bean.MapWrapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;
import java.util.stream.Collectors;

/**
 * auth user
 *
 * @author j.x
 * @date 2023/11/1 16:48
 * @since 0.1.0
 */
@Setter
@Getter
@AllArgsConstructor
public class TurboUser implements UserDetails {

    public static final String ACCOUNT_NON_EXPIRED_FIELD = "accountNonExpired";
    public static final String ACCOUNT_NON_LOCKED_FIELD = "accountNonLocked";
    public static final String CREDENTIALS_NON_EXPIRED_FIELD = "credentialsNonExpired";
    public static final String ACCOUNT_ENABLED_FIELD = "enabled";
    public static final String USER_ID_FIELD = "userId";
    public static final String AUTHORITIES_FIELD = "authorities";
    public static final String ROLE_ID_FIELD = "roleId";
    public static final String ROLE_CODE_FIELD = "roleCode";
    public static final String ROLE_NAME_FIELD = "roleName";
    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";
    public static final String EMAIL_FIELD = "email";
    public static final String PHONE_FIELD = "phone";
    public static final String AVATAR_FIELD = "avatar";
    public static final String NICKNAME_FIELD = "nickname";
    public static final String TENANT_ID_FIELD = "tenantId";
    public static final String ORG_ID_FIELD = "orgId";
    public static final String ADMINISTRATOR_FIELD = "administrator";

    public static final String ROLE_OF_ADMINISTRATOR = "administrator";

    private Set<TurboGrantedAuthority> authorities;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

    /**
     * user id
     */
    private Long userId;

    /**
     * user name
     */
    private String username;

    /**
     * user password
     */
    private String password;

    /**
     * user email
     */
    private String email;

    /**
     * user phone
     */
    private String phone;

    /**
     * avatar
     */
    private String avatar;

    /**
     * nickname
     */
    private String nickname;

    /**
     * belong to tenant
     */
    private Long tenantId;

    /**
     * 组织id
     */
    private Long orgId;

    /**
     * whether admin
     */
    private boolean administrator;

    public TurboUser(SysUserVO user) {
        this.authorities =
                user.getRoles()
                        .stream()
                        .map(role -> new TurboGrantedAuthority(role.getId(), role.getCode(), role.getName()))
                        .collect(Collectors.toSet());
        UserStatus userStatus = user.getStatus();
        this.accountNonLocked = userStatus != UserStatus.LOCK;
        this.accountNonExpired = true;
        this.credentialsNonExpired = true;
        this.enabled = userStatus == UserStatus.ENABLE;
        this.userId = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.avatar = user.getAvatar();
        this.nickname = user.getNickname();
        this.tenantId = user.getTenantId();
        this.orgId = user.getOrgId();
        // determinate authority has administrator
        this.administrator = authorities.stream().anyMatch(authorities -> authorities.getAuthority().equals(ROLE_OF_ADMINISTRATOR));
    }

    public TurboUser(Jwt jwt) {
        this.accountNonExpired = jwt.getClaimAsBoolean(ACCOUNT_NON_EXPIRED_FIELD);
        this.accountNonLocked = jwt.getClaimAsBoolean(ACCOUNT_NON_LOCKED_FIELD);
        this.credentialsNonExpired = jwt.getClaimAsBoolean(CREDENTIALS_NON_EXPIRED_FIELD);
        this.enabled = jwt.getClaimAsBoolean(ACCOUNT_ENABLED_FIELD);
        this.userId = jwt.getClaim(USER_ID_FIELD);
        List<Map<String, Object>> claimAuthorities = jwt.getClaim(AUTHORITIES_FIELD);
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
        this.email = jwt.getClaimAsString(EMAIL_FIELD);
        this.phone = jwt.getClaimAsString(PHONE_FIELD);
        this.avatar = jwt.getClaimAsString(AVATAR_FIELD);
        this.nickname = jwt.getClaimAsString(NICKNAME_FIELD);
        this.tenantId = jwt.getClaim(TENANT_ID_FIELD);
        this.orgId = jwt.getClaim(ORG_ID_FIELD);
        this.administrator = Optional.ofNullable(jwt.getClaimAsBoolean(ADMINISTRATOR_FIELD)).map(Boolean::booleanValue).orElse(false);
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
