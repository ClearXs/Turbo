package cc.allio.turbo.modules.auth.jwt;

import cc.allio.turbo.common.util.SecureUtil;
import com.google.common.collect.Lists;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.security.oauth2.jwt.*;

/**
 * decoration {@link NimbusJwtEncoder}
 *
 * @author j.x
 * @date 2024/8/28 14:16
 * @since 0.1.1
 */
public class TurboJwtEncoder implements JwtEncoder {

    private final JwtEncoder actual;

    private static TurboJwtEncoder instance;

    private TurboJwtEncoder() {
        RSAKey rsaKey =
                new RSAKey.Builder(SecureUtil.getSystemRasPublicKey())
                        .privateKey(SecureUtil.getSystemRasPrivateKey())
                        .keyID("turbo jwt")
                        .build();
        this.actual = new NimbusJwtEncoder((s, ctx) -> Lists.newArrayList(rsaKey));
    }

    @Override
    public Jwt encode(JwtEncoderParameters parameters) throws JwtEncodingException {
        return actual.encode(parameters);
    }

    /**
     * static method synchronize get {@link JwtEncoder} instance
     *
     * @return the {@link JwtEncoder} instance
     */
    public static JwtEncoder getInstance() {
        if (instance == null) {
            synchronized (TurboJwtEncoder.class) {
                instance = new TurboJwtEncoder();
            }
        }
        return instance;
    }
}
