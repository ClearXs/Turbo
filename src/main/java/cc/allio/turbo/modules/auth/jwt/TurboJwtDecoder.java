package cc.allio.turbo.modules.auth.jwt;

import cc.allio.turbo.common.util.SecureUtil;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.BadJWTException;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.text.ParseException;

/**
 * decoration {@link NimbusJwtDecoder}
 *
 * @author j.x
 * @date 2024/8/28 14:18
 * @since 0.1.1
 */
public class TurboJwtDecoder implements JwtDecoder {

    private final JwtDecoder actual;

    private static TurboJwtDecoder instance;

    private TurboJwtDecoder() {
        this.actual = new NimbusJwtDecoder(new InternalJWTProcessor());
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        return actual.decode(token);
    }

    /**
     * static method synchronize get {@link JwtDecoder} instance
     *
     * @return the {@link JwtDecoder} instance
     */
    public static JwtDecoder getInstance() {
        if (instance == null) {
            synchronized (TurboJwtDecoder.class) {
                instance = new TurboJwtDecoder();
            }
        }
        return instance;
    }

    static class InternalJWTProcessor extends DefaultJWTProcessor<SecurityContext> {
        JWSVerifier jwsVerifier = new RSASSAVerifier(SecureUtil.getSystemRasPublicKey());

        @Override
        public JWTClaimsSet process(SignedJWT signedJWT, SecurityContext context) throws BadJOSEException {
            try {
                if (signedJWT.verify(jwsVerifier)) {
                    return signedJWT.getJWTClaimsSet();
                }
            } catch (ParseException ex) {
                // Payload not a JSON object
                throw new BadJWTException(ex.getMessage(), ex);
            } catch (JOSEException e) {
                throw new RuntimeException(e);
            }
            return null;
        }
    }
}
