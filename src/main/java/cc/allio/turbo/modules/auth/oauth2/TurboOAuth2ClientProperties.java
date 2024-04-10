package cc.allio.turbo.modules.auth.oauth2;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "turbo.oauth2.client")
public class TurboOAuth2ClientProperties extends OAuth2ClientProperties {

    /**
     * redirect
     */
    private String domain;
}
