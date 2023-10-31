package cc.allio.uno.turbo.auth.provider;

import cc.allio.uno.turbo.system.entity.SysUser;
import cc.allio.uno.turbo.system.service.ISysUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
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
public class TurboUserServiceDetails implements UserDetailsService {

    private final ISysUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = userService.findByUsername(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException(String.format("username %s not found", username));
        }
        // 1.实现角色组织赋值
        // 2.实现权限授予
        return new User(sysUser.getUsername(), sysUser.getPassword(), Collections.emptyList());
    }
}
