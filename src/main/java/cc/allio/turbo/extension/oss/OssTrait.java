package cc.allio.turbo.extension.oss;

import lombok.Data;

/**
 * Oss相关特性信息
 *
 * @author j.x
 * @date 2023/11/17 15:42
 * @since 0.1.0
 */
@Data
public class OssTrait {

    /**
     * oss provider
     */
    private Provider provider;

    /**
     * 应用名称
     */
    private String application;

    /**
     * 连接端点
     */
    private String endpoint;

    /**
     * 存储空间
     */
    private String bucket;

    /**
     * 访问ID
     */
    private String accessKey;

    /**
     * 访问密钥
     */
    private String secretKey;

}
