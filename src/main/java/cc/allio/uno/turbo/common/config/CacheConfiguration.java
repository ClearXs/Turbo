package cc.allio.uno.turbo.common.config;

import cc.allio.uno.turbo.common.cache.Caches;
import cc.allio.uno.turbo.common.cache.TurboCache;
import cc.allio.uno.turbo.common.cache.TurboCacheManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class CacheConfiguration {

    @Bean
    public TurboCacheManager cacheRegistration(List<TurboCache> caches) {
        return new TurboCacheManager(caches);
    }

    @Bean
    @ConditionalOnBean(TurboCacheManager.class)
    public Caches caches() {
        return new Caches();
    }
}
