package cc.allio.turbo.modules.auth.authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;

import java.util.Map;

/**
 * 封装jwt，并且它是{@link org.springframework.security.core.Authentication}
 *
 * @author j.x
 * @date 2023/10/26 22:27
 * @since 0.1.0
 */
public class TurboJwtAuthenticationToken extends AbstractOAuth2TokenAuthenticationToken<Jwt> {

    public TurboJwtAuthenticationToken(Jwt jwt, UserDetails userDetails) {
        super(jwt, userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
    }

    @Override
    public Map<String, Object> getTokenAttributes() {
        return getToken().getClaims();
    }

    /**
     * get id
     *
     * @return id
     */
    public Long getUserId() {
        Object id = getTokenAttributes().get("userId");
        if (id != null) {
            return (Long) id;
        }
        return null;
    }
}
