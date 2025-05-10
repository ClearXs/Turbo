package cc.allio.turbo.modules.auth.jwt;

import cc.allio.turbo.modules.auth.configuration.SecurityConfiguration;
import cc.allio.turbo.modules.auth.properties.SecureProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;

@AutoConfiguration
@ImportAutoConfiguration({JwtCodecConfiguration.class, SecurityConfiguration.class})
@AutoConfigureAfter(SecurityConfiguration.class)
public class JwtConfiguration {

    @Bean
    public JwtAuthentication jwtAuthentication(JwtEncoder jwtEncoder,
                                               JwtDecoder jwtDecoder,
                                               SecureProperties secureProperties) {
        return new JwtAuthentication(secureProperties, jwtEncoder, jwtDecoder);
    }
}
