package cc.allio.uno.turbo.common.cache;

import org.springframework.cache.Cache;

import java.util.concurrent.TimeUnit;

/**
 * 包含过期设置的缓存操作
 *
 * @author j.x
 * @date 2023/10/27 14:47
 * @since 0.1.0
 */
public interface TurboCache<T> extends Cache {

    /**
     * 在缓存中是否存在指定的key
     *
     * @param key key
     * @return true or false
     */
    boolean hasKey(String key);

    /**
     * 设置key过期时间
     *
     * @param key      key
     * @param value    value
     * @param time     time
     * @param timeUnit timeUnit
     * @param <T>      泛型
     */
    default void setEx(String key, T value, long time, TimeUnit timeUnit) {
        setEx(key, () -> value, time, timeUnit);
    }

    /**
     * 设置key过期时间
     *
     * @param key      key
     * @param value    value
     * @param time     time
     * @param timeUnit timeUnit
     */
    void setEx(String key, ValueWrapper value, long time, TimeUnit timeUnit);

    /**
     * 获取指定key的剩余过期时间
     *
     * @param key key
     * @return expire for time
     */
    default Long getExpire(String key) {
        return getExpire(key, TimeUnit.MICROSECONDS);
    }

    /**
     * 获取指定key的剩余过期时间
     *
     * @param key  key
     * @param unit unit
     * @return expire for time
     */
    Long getExpire(String key, TimeUnit unit);
}
