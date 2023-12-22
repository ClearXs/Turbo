package cc.allio.turbo.common.mybatis;

import cc.allio.turbo.common.mybatis.handle.BaseChangeMetaObjectHandler;
import cc.allio.turbo.common.mybatis.injetor.TurboSqlInjector;
import cc.allio.turbo.common.mybatis.plugins.inner.ConstraintInnerInterceptor;
import cc.allio.turbo.common.mybatis.plugins.inner.SortableInnerInterceptor;
import cc.allio.turbo.common.mybatis.plugins.inner.TurboTenantLineHandler;
import cc.allio.turbo.common.persistent.PersistentProperties;
import cc.allio.turbo.common.mybatis.id.SnowflakeIdentifierGenerator;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureBefore(MybatisPlusAutoConfiguration.class)
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
    public TurboSqlInjector turboSqlInjector() {
        return new TurboSqlInjector();
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
        // 注解排序插件
        mybatisPlusInterceptor.addInnerInterceptor(new SortableInnerInterceptor());
        return mybatisPlusInterceptor;
    }

}
