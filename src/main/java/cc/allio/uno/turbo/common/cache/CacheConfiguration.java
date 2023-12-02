package cc.allio.uno.turbo.common.cache;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class CacheConfiguration {

    @Bean
    public TurboCacheManager cacheRegistration(List<TurboCache<?>> caches) {
        return new TurboCacheManager(caches);
    }

    @Bean
    @ConditionalOnBean(TurboCacheManager.class)
    public CacheHelper cacheHelper() {
        return new CacheHelper();
    }
}
