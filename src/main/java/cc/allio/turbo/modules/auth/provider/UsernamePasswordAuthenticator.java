package cc.allio.turbo.modules.auth.provider;

import cc.allio.turbo.modules.auth.params.LoginMode;
import cc.allio.turbo.modules.auth.params.LoginUsernamePassword;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * base on {@link LoginMode#EMPTY} authentication
 *
 * @author j.x
 * @date 2024/4/6 17:46
 * @since 0.1.1
 */
public class UsernamePasswordAuthenticator implements LoginClaimAuthenticator<LoginUsernamePassword> {

    @Override
    public UsernamePasswordAuthenticationToken authenticate(LoginUsernamePassword loginClaim) {
        return new UsernamePasswordAuthenticationToken(loginClaim.getUsername(), loginClaim.getPassword());
    }
}
