package cc.allio.turbo.common.db.uno;

import cc.allio.turbo.common.db.mybatis.TurboMybatisConfiguration;
import cc.allio.turbo.common.db.mybatis.plugins.inner.TurboTenantLineHandler;
import cc.allio.turbo.common.db.persistent.PersistentProperties;
import cc.allio.turbo.common.db.uno.interceptor.AuditInterceptor;
import cc.allio.turbo.common.db.uno.interceptor.LogicInterceptor;
import cc.allio.turbo.common.db.uno.interceptor.TenantInterceptor;
import cc.allio.uno.data.orm.executor.ExecutorInitializerAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(TurboMybatisConfiguration.class)
@AutoConfigureBefore(ExecutorInitializerAutoConfiguration.class)
public class TurboDbConfiguration {

    @Bean
    public AuditInterceptor turboAuditInterceptor() {
        return new AuditInterceptor();
    }

    @Bean
    public LogicInterceptor turboLogicInterceptor() {
        return new LogicInterceptor();
    }

    @Bean
    public TenantInterceptor turboTenantInterceptor(PersistentProperties persistentProperties) {
        return new TenantInterceptor(new TurboTenantLineHandler(persistentProperties.getTenant()));
    }

}
