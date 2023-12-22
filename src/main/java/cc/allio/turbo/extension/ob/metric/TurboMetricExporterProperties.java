package cc.allio.turbo.extension.ob.metric;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author jiangwei
 * @date 2023/12/22 10:01
 * @see <a href="https://opentelemetry.io/docs/instrumentation/java/exporters/">exporter in opertelemetry</a>
 * @since 0.1.0
 */
@Data
@ConfigurationProperties("turbo.ob.metric.exporter")
public class TurboMetricExporterProperties {

    /**
     * 是否开启
     */
    private boolean enabled = true;

    /**
     * measurement exposure inner port
     */
    private int port = 9464;
}
