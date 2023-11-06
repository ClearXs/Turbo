package cc.allio.uno.turbo.auth.userdetails;

import cc.allio.uno.turbo.system.service.ISysUserService;
import cc.allio.uno.turbo.system.vo.SysUserVO;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

/**
 * spring security 获取用户
 *
 * @author j.x
 * @date 2023/10/23 14:36
 * @since 1.0.0
 */
@AllArgsConstructor
public class TurboUserDetailsService implements UserDetailsService {

    private final ISysUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUserVO user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("username %s not found", username));
        }
        // 1.角色授权
        user.setRoles(Collections.emptyList());
        // 2.权限授予
        return new TurboUser(user);
    }
}
