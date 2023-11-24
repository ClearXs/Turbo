package cc.allio.uno.turbo.common.persistent;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(PersistentProperties.class)
public class PersistentConfiguration {
}
