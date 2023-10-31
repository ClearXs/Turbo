package cc.allio.uno.turbo.auth.config;

import cc.allio.uno.turbo.common.util.SecureUtil;
import com.google.common.collect.Lists;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.BadJWTException;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.text.ParseException;

@Configuration
public class JwtConfig {

    @Bean
    public JwtEncoder turboJwtEncoder() {
        RSAKey rsaKey = new RSAKey.Builder(SecureUtil.getSystemRasPublicKey())
                .privateKey(SecureUtil.getSystemRasPrivateKey())
                .keyID("turbo jwt")
                .build();
        return new NimbusJwtEncoder((s, ctx) -> Lists.newArrayList(rsaKey));
    }

    @Bean
    public JwtDecoder turboJwtDecoder() {
        JWSVerifier jwsVerifier = new RSASSAVerifier(SecureUtil.getSystemRasPublicKey());
        return new NimbusJwtDecoder(new DefaultJWTProcessor<>() {
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
        });
    }
}
