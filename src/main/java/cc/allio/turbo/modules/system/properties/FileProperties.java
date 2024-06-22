package cc.allio.turbo.modules.system.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "turbo.file")
public class FileProperties {

    private Upload upload;

    @Data
    public static class Upload {

        /**
         * 文件上传最大大小 10485760 = 10mb
         */
        private long maxSize = 10485760;
    }
}
