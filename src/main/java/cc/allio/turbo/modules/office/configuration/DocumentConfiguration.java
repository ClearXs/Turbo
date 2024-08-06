package cc.allio.turbo.modules.office.configuration;

import cc.allio.turbo.modules.office.configuration.properties.DocumentProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(DocumentProperties.class)
public class DocumentConfiguration {
}
