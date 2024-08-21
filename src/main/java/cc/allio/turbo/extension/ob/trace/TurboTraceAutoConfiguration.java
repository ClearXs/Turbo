package cc.allio.turbo.extension.ob.trace;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TurboTraceExporterProperties.class)
@ConditionalOnProperty(prefix = "turbo.ob.trace.exporter", name = "enabled", havingValue = "true", matchIfMissing = true)
public class TurboTraceAutoConfiguration {

}
