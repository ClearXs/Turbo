package cc.allio.turbo.common.cache;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.StringUtils;
import org.springframework.cache.Cache;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * definition bridge of {@link TurboCache}
 *
 * @author j.x
 * @date 2024/3/29 00:03
 * @since 0.1.1
 */
public interface ICache extends TurboCache, CacheName {

    /**
     * 根据指定的key获取缓存中的值
     *
     * @param key         the key
     * @param classType   bean class type
     * @param valueLoader value loader
     * @param <T>         type
     * @return value or null
     */
    default <T> T get(Object key, Class<T> classType, Callable<T> valueLoader) {
        return Optional.ofNullable(obtainCache()).map(cache -> cache.get(key, classType, valueLoader)).orElse(null);
    }

    /**
     * 在缓存中是否存在指定的key
     *
     * @param key key
     * @return true or false
     */
    default boolean hasKey(String key) {
        return Optional.ofNullable(obtainCache()).map(cache -> cache.hasKey(key)).orElse(false);
    }

    /**
     * 设置key过期时间
     *
     * @param key      key
     * @param value    value
     * @param time     time
     * @param timeUnit timeUnit
     */
    default void setEx(String key, ValueWrapper value, long time, TimeUnit timeUnit) {
        Optional.ofNullable(obtainCache()).ifPresent(cache -> cache.setEx(key, value, time, timeUnit));
    }

    /**
     * 获取指定key的剩余过期时间
     *
     * @param key  key
     * @param unit unit
     * @return expire for time
     */
    default Long getExpire(String key, TimeUnit unit) {
        return Optional.ofNullable(obtainCache()).map(cache -> cache.getExpire(key, unit)).orElse(null);
    }

    /**
     * 移除指定key的缓存
     *
     * @param key key
     * @return true if remove else not
     */
    default boolean remove(String key) {
        return Boolean.TRUE.equals(Optional.ofNullable(obtainCache()).map(cache -> cache.remove(key)).orElse(null));
    }

    /**
     * 移除指定key的缓存
     *
     * @param keys key
     * @return true if remove else not
     */
    default boolean remove(Collection<?> keys) {
        return Boolean.TRUE.equals(Optional.ofNullable(obtainCache()).map(cache -> cache.remove(keys)).orElse(null));
    }

    @Override
    default String getName() {
        return Optional.ofNullable(obtainCache()).map(Cache::getName).orElse(StringPool.EMPTY);
    }

    @Override
    default Object getNativeCache() {
        return Optional.ofNullable(obtainCache()).map(Cache::getNativeCache).orElse(null);
    }

    @Override
    default ValueWrapper get(Object key) {
        return Optional.ofNullable(obtainCache()).map(cache -> cache.get(key)).orElse(null);
    }

    @Override
    default <T> T get(Object key, Class<T> type) {
        return null;
    }

    @Override
    default void put(Object key, Object value) {
        Optional.ofNullable(obtainCache()).ifPresent(cache -> cache.put(key, value));
    }

    @Override
    default void evict(Object key) {
        Optional.ofNullable(obtainCache()).ifPresent(cache -> cache.evict(key));
    }

    @Override
    default void clear() {
        Optional.ofNullable(obtainCache()).ifPresent(Cache::clear);
    }

    /**
     * obtain {@link TurboCache} instance
     *
     * @return TurboCache or null
     */
    default TurboCache obtainCache() {
        String cacheName = getCacheName();
        if (StringUtils.isBlank(cacheName)) {
            return null;
        }
        return CacheHelper.getIfAbsent(cacheName);
    }
}