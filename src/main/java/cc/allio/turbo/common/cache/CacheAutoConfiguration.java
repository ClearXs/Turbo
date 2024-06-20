package cc.allio.turbo.common.cache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class CacheAutoConfiguration {

    @Bean
    public TurboCacheManager cacheRegistration(List<TurboCache> caches) {
        TurboCacheManager cacheManager = new TurboCacheManager(caches);
        CacheHelper.cacheManager = cacheManager;
        return cacheManager;
    }
}
