package cc.allio.turbo.extension.ob.log;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(OpenObserverProperties.class)
@ConditionalOnProperty(prefix = "turbo.ob.log.openobserve", name = "enabled", havingValue = "true", matchIfMissing = true)
public class TurboLogAutoConfiguration {

}
