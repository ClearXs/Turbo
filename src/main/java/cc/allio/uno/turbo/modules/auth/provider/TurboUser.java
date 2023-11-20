package cc.allio.uno.turbo.modules.auth.provider;

import cc.allio.uno.turbo.modules.system.constant.UserStatus;
import cc.allio.uno.turbo.modules.system.vo.SysUserVO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * auth user
 *
 * @author j.x
 * @date 2023/11/1 16:48
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public class TurboUser implements UserDetails {

    private final Set<GrantedAuthority> authorities;

    private final boolean accountNonExpired;

    private final boolean accountNonLocked;

    private final boolean credentialsNonExpired;

    private final boolean enabled;

    /**
     * 用户id
     */
    private final long userId;

    /**
     * 用户名
     */
    private final String username;

    /**
     * 密码
     */
    private final String password;

    /**
     * 邮箱
     */
    private final String email;

    /**
     * 电话号码
     */
    private final String phone;

    /**
     * 头像
     */
    private final String avatar;

    /**
     * 昵称
     */
    private final String nickname;


    public TurboUser(SysUserVO user) {
        this.authorities = user.getRoles()
                .stream()
                .map(role -> new OAuth2UserAuthority(role.getCode(), Collections.emptyMap()))
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
    }

    public TurboUser(Jwt jwt) {
        this.accountNonExpired = jwt.getClaimAsBoolean("accountNonExpired");
        this.accountNonLocked = jwt.getClaimAsBoolean("accountNonLocked");
        this.credentialsNonExpired = jwt.getClaimAsBoolean("credentialsNonExpired");
        this.enabled = jwt.getClaimAsBoolean("enabled");
        this.userId = jwt.getClaim("userId");
        List<String> claimAuthorities = jwt.getClaimAsStringList("authorities");
        this.authorities = claimAuthorities.stream()
                .map(auth -> new OAuth2UserAuthority(auth, Collections.emptyMap()))
                .collect(Collectors.toSet());
        this.username = jwt.getClaimAsString("username");
        this.password = jwt.getClaimAsString("password");
        this.email = jwt.getClaimAsString("email");
        this.phone = jwt.getClaimAsString("phone");
        this.avatar = jwt.getClaimAsString("avatar");
        this.nickname = jwt.getClaimAsString("nickname");
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
