package cc.allio.turbo.modules.auth.provider;

import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.modules.system.service.ISysUserService;
import cc.allio.turbo.modules.system.domain.SysUserVO;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * spring security 获取用户
 *
 * @author j.x
 * @date 2023/10/23 14:36
 * @since 0.1.0
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
