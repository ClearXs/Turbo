package cc.allio.turbo.extension.ob.metric;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TurboMetricExporterProperties.class)
@ConditionalOnProperty(prefix = "turbo.ob.metric.exporter", name = "enabled", havingValue = "true", matchIfMissing = true)
public class TurboMetricAutoConfiguration {

}
