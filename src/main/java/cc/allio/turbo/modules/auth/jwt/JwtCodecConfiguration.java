package cc.allio.turbo.modules.auth.jwt;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;

@AutoConfiguration
public class JwtCodecConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public JwtEncoder turboJwtEncoder() {
        return TurboJwtEncoder.getInstance();
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtDecoder turboJwtDecoder() {
        return TurboJwtDecoder.getInstance();
    }
}
