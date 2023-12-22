package cc.allio.turbo.extension.ob.trace;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("turbo.ob.trace.exporter")
public class TurboTraceExporterProperties {

    /**
     * 是否开启
     */
    private boolean enabled = true;
    private String endpoint = "http://127.0.0.1:9411/api/v2/spans";
}
