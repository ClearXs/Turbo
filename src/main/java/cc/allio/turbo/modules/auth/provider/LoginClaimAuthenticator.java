package cc.allio.turbo.modules.auth.provider;

import cc.allio.turbo.modules.auth.params.LoginClaim;
import cc.allio.turbo.modules.auth.params.LoginMode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * login claim authenticator. support several {@link LoginMode} login methods
 *
 * @author j.x
 * @date 2024/4/6 17:42
 * @since 0.1.1
 */
public interface LoginClaimAuthenticator<T extends LoginClaim> {

    /**
     * authenticate the login claim.
     * <p>it will be verify login code, like as captcha code, verification code...</p>
     *
     * @param loginClaim the login claim
     * @return an {@link UsernamePasswordAuthenticationToken} instance
     */
    UsernamePasswordAuthenticationToken authenticate(T loginClaim);
}
