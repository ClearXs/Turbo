package cc.allio.uno.turbo.modules.auth.provider;

import cc.allio.uno.turbo.common.exception.BizException;
import cc.allio.uno.turbo.modules.system.service.ISysUserService;
import cc.allio.uno.turbo.modules.system.vo.SysUserVO;
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
        SysUserVO user = null;
        try {
            user = userService.findByUsername(username);
        } catch (BizException e) {
            throw new UsernameNotFoundException(String.format("username %s not found", username));
        }
        return new TurboUser(user);
    }
}
