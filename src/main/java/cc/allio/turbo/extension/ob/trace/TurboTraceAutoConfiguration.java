package cc.allio.turbo.extension.ob.trace;

import io.opentelemetry.exporter.zipkin.ZipkinSpanExporter;
import io.opentelemetry.exporter.zipkin.ZipkinSpanExporterBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TurboTraceExporterProperties.class)
@ConditionalOnClass(ZipkinSpanExporter.class)
@ConditionalOnProperty(prefix = "turbo.ob.trace.exporter", name = "enabled", havingValue = "true", matchIfMissing = true)
public class TurboTraceAutoConfiguration {

    @Bean
    @ConditionalOnClass
    public ZipkinSpanExporter turboZipkinSpanExporter(TurboTraceExporterProperties traceExporterProperties) {
        ZipkinSpanExporterBuilder zipkinSpanExporterBuilder = ZipkinSpanExporter.builder();
        zipkinSpanExporterBuilder.setEndpoint(traceExporterProperties.getEndpoint());
        return zipkinSpanExporterBuilder.build();
    }
}
