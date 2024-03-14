package cc.allio.turbo.extension.ob.log;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;

/**
 * @author j.x
 * @date 2023/12/22 10:01
 * @see <a href="https://opentelemetry.io/docs/instrumentation/java/exporters/">exporter in opertelemetry</a>
 * @since 0.1.0
 */
@Data
@ConfigurationProperties("turbo.ob.log.exporter")
public class TurboLogExporterProperties {

    /**
     * 是否开启
     */
    private boolean enabled = true;

    /**
     * exporter endpoint by default openobserve
     */
    private String endpoint = "http://127.0.0.1:5080/api/default/default/_json";

    /**
     * exporter timeout
     */
    private Duration timeout = Duration.ofSeconds(10);

    private boolean writeToJson = true;
    private Map<String, String> headers = Collections.emptyMap();
}
