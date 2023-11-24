package cc.allio.uno.turbo.common.mybatis;

import cc.allio.uno.turbo.common.mybatis.handle.BaseChangeMetaObjectHandler;
import cc.allio.uno.turbo.common.mybatis.id.SnowflakeIdentifierGenerator;
import cc.allio.uno.turbo.common.mybatis.plugins.inner.ConstraintInnerInterceptor;
import cc.allio.uno.turbo.common.mybatis.plugins.inner.TurboTenantLineHandler;
import cc.allio.uno.turbo.common.persistent.PersistentProperties;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
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

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(PersistentProperties persistentProperties) {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        // 多租户插件配置,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存万一出现问题
        // 租户插件
        mybatisPlusInterceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TurboTenantLineHandler(persistentProperties.getTenant())));
        // 分页插件
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        // 乐观锁插件
        mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        // 数据约束插件
        mybatisPlusInterceptor.addInnerInterceptor(new ConstraintInnerInterceptor());
        return mybatisPlusInterceptor;
    }

}
