package cc.allio.turbo.common.db.persistent;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(PersistentProperties.class)
public class PersistentConfiguration {
}
