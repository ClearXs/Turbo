package cc.allio.uno.turbo.auth.userdetails;

import cc.allio.uno.turbo.system.constant.UserStatus;
import cc.allio.uno.turbo.system.vo.SysUserVO;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

import java.util.Collection;
import java.util.Collections;

/**
 * auth user
 *
 * @author j.x
 * @date 2023/11/1 16:48
 * @since 1.0.0
 */
@Getter
public class TurboUser extends User {

    private final long userId;
    private final String username;

    public TurboUser(SysUserVO sysUserVO) {
        super(
                sysUserVO.getUsername(),
                sysUserVO.getPassword(),
                // 账户生效信息
                sysUserVO.getStatus() == UserStatus.ENABLE,
                sysUserVO.getStatus() == UserStatus.ENABLE,
                sysUserVO.getStatus() == UserStatus.ENABLE,
                sysUserVO.getStatus() == UserStatus.ENABLE,
                // 权限信息
                sysUserVO.getRoles()
                        .stream()
                        .map(role -> new OAuth2UserAuthority(role.getCode(), Collections.emptyMap()))
                        .toList()
        );
        this.userId = sysUserVO.getId();
        this.username = sysUserVO.getUsername();
    }

    public TurboUser(long userId, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = userId;
        this.username = username;
    }
}
