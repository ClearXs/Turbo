package cc.allio.turbo.common.db;

import cc.allio.turbo.common.db.mybatis.handle.BaseChangeMetaObjectHandler;
import cc.allio.turbo.common.db.mybatis.injetor.TurboSqlInjector;
import cc.allio.turbo.common.db.mybatis.plugins.inner.ConstraintInnerInterceptor;
import cc.allio.turbo.common.db.mybatis.plugins.inner.SortableInnerInterceptor;
import cc.allio.turbo.common.db.mybatis.plugins.inner.TurboTenantLineHandler;
import cc.allio.turbo.common.db.persistent.PersistentProperties;
import cc.allio.turbo.common.db.id.SnowflakeIdentifierGenerator;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(MybatisPlusAutoConfiguration.class)
public class TurboDbConfiguration {

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
