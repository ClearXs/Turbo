package cc.allio.turbo.modules.system.properties;

import cc.allio.turbo.common.util.InetUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "turbo.file")
public class FileProperties {

    private Upload upload;

    @Data
    public static class Upload {

        /**
         * 文件上传地址.
         * <ol>
         *     <li>开发地址：http://localhost:8600</li>
         *     <li>在服务器上: http://43.143.195.208/</li>
         *     <li>通过nginx转发的：http://43.143.195.208/api</li>
         *     <li>https协议的：https://43.143.195.208/</li>
         *     <li>带有的域名的：https://allio.cc</li>
         * </ol>
         * <p>基于以上这些情况需要进行配置化，来觉得文件存储路径</p>
         *
         * @see InetUtil#getHttpSelfAddress()
         */
        private String path;

        /**
         * 文件上传最大大小 10485760 = 10mb
         */
        private long maxSize = 10485760;
    }
}
