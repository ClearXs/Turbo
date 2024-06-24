package cc.allio.turbo.extension.oss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * describe oss properties
 *
 * @author j.x
 * @date 2024/6/23 17:11
 * @since 0.1.1
 */
@Data
@ConfigurationProperties("turbo.oss")
public class OssProperties {

    /**
     * base
     */
    private String baseDir = "turbo";

    /**
     * naming
     */
    private Path.AppendStrategy strategy = Path.AppendStrategy.Date;
}
