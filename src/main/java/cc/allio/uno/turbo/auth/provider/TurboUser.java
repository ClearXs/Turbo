package cc.allio.uno.turbo.auth.provider;

import cc.allio.uno.turbo.system.constant.UserStatus;
import cc.allio.uno.turbo.system.vo.SysUserVO;
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

    private final long userId;

    private final String password;

    private final String username;

    private final Set<GrantedAuthority> authorities;

    private final boolean accountNonExpired;

    private final boolean accountNonLocked;

    private final boolean credentialsNonExpired;

    private final boolean enabled;

    public TurboUser(SysUserVO user) {
        this.userId = user.getId();
        this.password = user.getPassword();
        this.username = user.getUsername();
        this.authorities = user.getRoles()
                .stream()
                .map(role -> new OAuth2UserAuthority(role.getCode(), Collections.emptyMap()))
                .collect(Collectors.toSet());
        UserStatus userStatus = user.getStatus();

        this.accountNonLocked = userStatus != UserStatus.LOCK;
        this.accountNonExpired = true;
        this.credentialsNonExpired = true;
        this.enabled = userStatus == UserStatus.ENABLE;
    }

    public TurboUser(Jwt jwt) {
        this.userId = jwt.getClaim("userId");
        this.username = jwt.getClaimAsString("username");
        this.password = jwt.getClaimAsString("password");
        List<String> claimAuthorities = jwt.getClaimAsStringList("authorities");
        this.authorities = claimAuthorities.stream()
                .map(auth -> new OAuth2UserAuthority(auth, Collections.emptyMap()))
                .collect(Collectors.toSet());
        this.accountNonExpired = jwt.getClaimAsBoolean("accountNonExpired");
        this.accountNonLocked = jwt.getClaimAsBoolean("accountNonLocked");
        this.credentialsNonExpired = jwt.getClaimAsBoolean("credentialsNonExpired");
        this.enabled = jwt.getClaimAsBoolean("enabled");
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
