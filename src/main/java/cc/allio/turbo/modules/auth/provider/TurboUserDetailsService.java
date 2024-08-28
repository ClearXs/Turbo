package cc.allio.turbo.modules.auth.provider;

import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.modules.auth.authority.TurboGrantedAuthority;
import cc.allio.turbo.modules.system.entity.SysRole;
import cc.allio.turbo.modules.system.service.ISysUserService;
import cc.allio.turbo.modules.system.domain.SysUserVO;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        SysUserVO user;
        try {
            user = userService.findByUsername(username);
        } catch (BizException e) {
            throw new UsernameNotFoundException(String.format("username %s not found", username));
        }
        List<SysRole> roles = user.getRoles();
        Set<TurboGrantedAuthority> authorities =
                roles.stream()
                        .map(role -> new TurboGrantedAuthority(role.getId(), role.getCode(), role.getName()))
                        .collect(Collectors.toSet());
        return new TurboUser(user, authorities);
    }
}
