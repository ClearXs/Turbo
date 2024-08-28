package cc.allio.turbo.modules.auth.jwt;

import cc.allio.turbo.modules.auth.properties.SecureProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;

@Configuration
public class JwtConfiguration {

    @Bean
    public JwtEncoder turboJwtEncoder() {
        return new TurboJwtEncoder();
    }

    @Bean
    public JwtDecoder turboJwtDecoder() {
        return new TurboJwtDecoder();
    }

    @Bean
    public JwtAuthentication jwtAuthentication(JwtEncoder jwtEncoder,
                                               JwtDecoder jwtDecoder,
                                               SecureProperties secureProperties) {
        return new JwtAuthentication(secureProperties, jwtEncoder, jwtDecoder);
    }
}
