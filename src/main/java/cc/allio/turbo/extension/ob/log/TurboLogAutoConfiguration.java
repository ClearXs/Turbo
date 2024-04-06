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
@EnableConfigurationProperties(OpenObserverProperties.class)
@ConditionalOnClass(LogRecordExporter.class)
@ConditionalOnProperty(prefix = "turbo.ob.log.openobserve", name = "enabled", havingValue = "true", matchIfMissing = true)
public class TurboLogAutoConfiguration {

    private static final String URL = "/api/%s/%s/_json";

    @Bean
    @ConditionalOnMissingBean
    public OpenObserveHttpLogRecordExporter openObserveHttpLogRecordExporter(OpenObserverProperties openObserverProperties) {
        OpenObverseHttpLogRecordExporterBuilder logRecordExporterBuilder = new OpenObverseHttpLogRecordExporterBuilder();
        String endpoint = openObserverProperties.getEndpoint();
        String path = String.format(URL, openObserverProperties.getOrganization(), openObserverProperties.getStream());
        logRecordExporterBuilder.setEndpoint(endpoint + path);
        Map<String, String> headers = openObserverProperties.getHeaders();
        if (CollectionUtils.isNotEmpty(headers)) {
            logRecordExporterBuilder.setHeaders(() -> headers);
        }
        if (openObserverProperties.isWriteToJson()) {
            logRecordExporterBuilder.writeJson();
        }
        logRecordExporterBuilder.setTimeout(openObserverProperties.getTimeout());
        return logRecordExporterBuilder.build();
    }
}
