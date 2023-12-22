package cc.allio.turbo.modules.auth.provider;

import cc.allio.turbo.modules.system.constant.UserStatus;
import cc.allio.turbo.modules.system.vo.SysUserVO;
import cc.allio.uno.core.bean.MapWrapper;
import cc.allio.turbo.modules.auth.authority.TurboGrantedAuthority;
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

    private Set<TurboGrantedAuthority> authorities;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 租户
     */
    private Long tenantId;

    /**
     * 组织id
     */
    private Long orgId;

    public TurboUser(SysUserVO user) {
        this.authorities = user.getRoles()
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
    }

    public TurboUser(Jwt jwt) {
        this.accountNonExpired = jwt.getClaimAsBoolean("accountNonExpired");
        this.accountNonLocked = jwt.getClaimAsBoolean("accountNonLocked");
        this.credentialsNonExpired = jwt.getClaimAsBoolean("credentialsNonExpired");
        this.enabled = jwt.getClaimAsBoolean("enabled");
        this.userId = jwt.getClaim("userId");
        List<Map<String, Object>> claimAuthorities = jwt.getClaim("authorities");
        this.authorities = claimAuthorities.stream()
                .map(auth -> {
                    MapWrapper wrapper = new MapWrapper(auth);
                    Long roleId = wrapper.getForce("roleId", Long.class);
                    String roleCode = wrapper.getForce("roleCode", String.class);
                    String roleName = wrapper.getForce("roleName", String.class);
                    return new TurboGrantedAuthority(roleId, roleCode, roleName);
                })
                .collect(Collectors.toSet());
        this.username = jwt.getClaimAsString("username");
        this.password = jwt.getClaimAsString("password");
        this.email = jwt.getClaimAsString("email");
        this.phone = jwt.getClaimAsString("phone");
        this.avatar = jwt.getClaimAsString("avatar");
        this.nickname = jwt.getClaimAsString("nickname");
        this.tenantId = jwt.getClaim("tenantId");
        this.orgId = jwt.getClaim("orgId");
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
