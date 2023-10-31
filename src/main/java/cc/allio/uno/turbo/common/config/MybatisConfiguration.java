package cc.allio.uno.turbo.common.config;

import cc.allio.uno.turbo.common.mybatis.BaseChangeMetaObjectHandler;
import cc.allio.uno.turbo.common.mybatis.id.SnowflakeIdentifierGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisConfiguration {

    @Bean
    public BaseChangeMetaObjectHandler baseChangeMetaObjectHandler() {
        return new BaseChangeMetaObjectHandler();
    }

    @Bean
    public SnowflakeIdentifierGenerator snowflakeIdentifierGenerator() {
        return new SnowflakeIdentifierGenerator();
    }

}
