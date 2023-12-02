package cc.allio.uno.turbo.common.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * turbo 自定义实现的cache管理器
 *
 * @author j.x
 * @date 2023/10/27 16:10
 * @since 1.0.0
 */
public class TurboCacheManager extends AbstractCacheManager {

    private final List<TurboCache<?>> caches;

    public TurboCacheManager(List<TurboCache<?>> caches) {
        this.caches = caches;
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        return caches;
    }

    /**
     * 如果没有就创建默认的cache
     *
     * @param cacheName cacheName must be unique
     * @return TurboCache
     */
    public TurboCache getIfAbsent(String cacheName) {
        Cache cache = getCache(cacheName);
        if (cache == null) {
            createCache(cacheName);
        }
        return (TurboCache) getCache(cacheName);
    }

    /**
     * 如果没有就根据supplier获取实例
     *
     * @param cacheName cacheName must be unique
     * @return TurboCache
     */
    public TurboCache computeIfAbsent(String cacheName, Supplier<? extends TurboCache> supplier) {
        Cache cache = getCache(cacheName);
        if (cache == null) {
            TurboCache turboCache = supplier.get();
            createCache(turboCache);
        }
        return (TurboCache) getCache(cacheName);
    }

    /**
     * 创建默认cache
     *
     * @param cacheName cacheName
     */
    public void createCache(String cacheName) {
        TurboCache<?> defaultCache = new DefaultTurboCache<>(cacheName);
        caches.add(defaultCache);
        initializeCaches();
    }

    /**
     * 根据参数创建cache实例创建cache
     *
     * @param cache cache instance
     */
    public void createCache(TurboCache<?> cache) {
        caches.add(cache);
        initializeCaches();
    }
}
