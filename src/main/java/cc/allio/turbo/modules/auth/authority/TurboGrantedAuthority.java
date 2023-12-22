package cc.allio.turbo.modules.auth.authority;

import cc.allio.uno.core.util.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@AllArgsConstructor
public class TurboGrantedAuthority implements GrantedAuthority {

    private final long roleId;
    private final String roleCode;
    private final String roleName;

    @Override
    public String getAuthority() {
        return roleCode;
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
