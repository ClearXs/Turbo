package cc.allio.turbo.extension.oss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * describe oss properties. now support to <b>Minio</b>. <b>aliyun oss</b>
 *
 * @author j.x
 * @date 2024/6/23 17:11
 * @since 0.1.1
 */
@Data
@ConfigurationProperties("turbo.oss")
public class OssProperties {

    /**
     * whether force use properties attributes
     */
    private Boolean fetch = false;

    /**
     * oss provider
     */
    private Provider provider = Provider.MINIO;

    /**
     * oss endpoint. like as http://localhost:9000
     */
    private String endpoint = "http://localhost:9000";

    /**
     * oss access key
     */
    private String accessKey = "admin";

    /**
     * oss secret key
     */
    private String secretKey = "admin";

    /**
     * oss bucket
     */
    private String bucket = "turbo";

    /**
     * base directory
     */
    private String baseDir = "/turbo";

    /**
     * path naming
     */
    private Path.AppendStrategy strategy = Path.AppendStrategy.None;

    public OssTrait copyTo() {
        OssTrait ossTrait = new OssTrait();
        ossTrait.setProvider(provider);
        ossTrait.setEndpoint(endpoint);
        ossTrait.setAccessKey(accessKey);
        ossTrait.setSecretKey(secretKey);
        ossTrait.setBucket(bucket);
        ossTrait.setBaseDir(baseDir);
        ossTrait.setStrategy(strategy);
        return ossTrait;
    }
}
