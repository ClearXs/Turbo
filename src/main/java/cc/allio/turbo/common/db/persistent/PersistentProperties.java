package cc.allio.turbo.common.db.persistent;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "turbo.persistent")
public class PersistentProperties {

    private Tenant tenant = new Tenant();

    @Data
    public static class Tenant {

        /**
         * 租户持久化字段名称
         */
        private String field = "tenant_id";

        /**
         * 忽略租户的表名列表
         */
        private List<String> ignoreList = Lists.newArrayList();

        /**
         * default tenant id
         */
        private String defaultTenantId = "0";
    }
}
