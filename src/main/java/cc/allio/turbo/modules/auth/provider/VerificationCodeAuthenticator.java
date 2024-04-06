package cc.allio.turbo.modules.auth.provider;

import cc.allio.turbo.modules.auth.params.LoginMode;
import cc.allio.turbo.modules.auth.params.LoginVerification;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * base on {@link LoginMode#VERIFICATION_CODE} authentication
 *
 * @author j.x
 * @date 2024/4/6 17:49
 * @since 0.1.1
 */
public class VerificationCodeAuthenticator implements LoginClaimAuthenticator<LoginVerification> {

    @Override
    public UsernamePasswordAuthenticationToken authenticate(LoginVerification loginClaim) {
        return null;
    }
}
