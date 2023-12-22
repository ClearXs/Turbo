package cc.allio.turbo.extension.ob.log;

import cc.allio.uno.core.util.CollectionUtils;
import io.opentelemetry.sdk.logs.export.LogRecordExporter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@EnableConfigurationProperties(TurboLogExporterProperties.class)
@ConditionalOnClass(LogRecordExporter.class)
@ConditionalOnProperty(prefix = "turbo.ob.log.exporter", name = "enabled", havingValue = "true", matchIfMissing = true)
public class TurboLogAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OpenObserveHttpLogRecordExporter openObserveHttpLogRecordExporter(TurboLogExporterProperties logExporterProperties) {
        OpenObverseHttpLogRecordExporterBuilder logRecordExporterBuilder = new OpenObverseHttpLogRecordExporterBuilder();
        logRecordExporterBuilder.setEndpoint(logExporterProperties.getEndpoint());
        Map<String, String> headers = logExporterProperties.getHeaders();
        if (CollectionUtils.isNotEmpty(headers)) {
            logRecordExporterBuilder.setHeaders(() -> headers);
        }
        if (logExporterProperties.isWriteToJson()) {
            logRecordExporterBuilder.writeJson();
        }
        logRecordExporterBuilder.setTimeout(logExporterProperties.getTimeout());
        return logRecordExporterBuilder.build();
    }
}
