package cc.allio.turbo.extension.ob.metric;

import io.opentelemetry.exporter.prometheus.PrometheusHttpServer;
import io.opentelemetry.exporter.prometheus.PrometheusHttpServerBuilder;
import io.opentelemetry.sdk.metrics.data.MetricData;
import io.opentelemetry.sdk.metrics.export.MetricProducer;
import io.opentelemetry.sdk.resources.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

@Configuration
@EnableConfigurationProperties(TurboMetricExporterProperties.class)
@ConditionalOnClass(PrometheusHttpServer.class)
@ConditionalOnProperty(prefix = "turbo.ob.metric.exporter", name = "enabled", havingValue = "true", matchIfMissing = true)
public class TurboMetricAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public PrometheusHttpServer turboPrometheusHttpServer(TurboMetricExporterProperties metricExporterProperties) {
        PrometheusHttpServerBuilder prometheusHttpServerBuilder = PrometheusHttpServer.builder();
        prometheusHttpServerBuilder.setHost("0.0.0.0");
        prometheusHttpServerBuilder.setPort(metricExporterProperties.getPort());
        return prometheusHttpServerBuilder.build();
    }

    @Bean
    public MetricProducer turboMetricProducer() {


        return new MetricProducer() {
            @Override
            public Collection<MetricData> produce(Resource resource) {
                return null;
            }
        };
    }
}
