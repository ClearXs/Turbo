package cc.allio.turbo.modules.system.configuration;

import cc.allio.turbo.modules.system.properties.FileProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(FileProperties.class)
public class FileConfiguration {
}
