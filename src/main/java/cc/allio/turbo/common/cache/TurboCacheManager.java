package cc.allio.turbo.common.cache;

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
 * @since 0.1.0
 */
public class TurboCacheManager extends AbstractCacheManager {

    private final List<TurboCache> caches;

    public TurboCacheManager(List<TurboCache> caches) {
        this.caches = caches;
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        return caches;
    }

    /**
     * @param cacheName lambda supplier cache name
     * @return TurboCache instance
     * @see #getCache(String)
     */
    public TurboCache getCache(Supplier<String> cacheName) {
        return (TurboCache) getCache(cacheName.get());
    }

    /**
     * @param cacheName CacheName interface for cache name
     * @return TurboCache instance
     * @see #getCache(String)
     */
    public TurboCache getCache(CacheName cacheName) {
        return (TurboCache) getCache(cacheName.getCacheName());
    }

    /**
     * @param cacheName lambda supplier cache name
     * @return TurboCache
     * @see #getIfAbsent(String)
     */
    public TurboCache getIfAbsent(Supplier<String> cacheName) {
        return getIfAbsent(cacheName.get());
    }

    /**
     * @param cacheName CacheName interface for cache name
     * @return TurboCache
     * @see #getIfAbsent(String)
     */
    public TurboCache getIfAbsent(CacheName cacheName) {
        return getIfAbsent(cacheName.getCacheName());
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
     * @param cacheName CacheName interface for cache name
     * @param supplier  laze supply to cache instance
     * @return TurboCache
     * @see #computeIfAbsent(String, Supplier)
     */
    public TurboCache computeIfAbsent(CacheName cacheName, Supplier<? extends TurboCache> supplier) {
        return computeIfAbsent(cacheName.getCacheName(), supplier);
    }


    /**
     * 如果没有就根据supplier获取实例
     *
     * @param cacheName cacheName must be unique
     * @param supplier  laze supply to cache instance
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
        caches.add(new TurboRedisCacheImpl(cacheName));
        initializeCaches();
    }

    /**
     * 根据参数创建cache实例创建cache
     *
     * @param cache cache instance
     */
    public void createCache(TurboCache cache) {
        caches.add(cache);
        initializeCaches();
    }
}
