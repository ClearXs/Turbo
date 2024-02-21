package cc.allio.turbo.common.cache;

import org.springframework.cache.Cache;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Cache for turbo, declaration cache operate, derivation {@link Cache} and expand, like contains expired 'set' operate etc...
 * <p>
 * noteworthy turbo cache with 'bean' operate, so you must specify bean class type
 * </p>
 *
 * @author j.x
 * @date 2023/10/27 14:47
 * @since 0.1.0
 */
public interface TurboCache extends Cache {

    /**
     * @see #get(Object, Class, Callable)
     * @deprecated
     */
    @Override
    @Deprecated
    default <T> T get(Object key, Callable<T> valueLoader) {
        return null;
    }

    /**
     * 根据指定的key获取缓存中的值
     *
     * @param key         the key
     * @param classType   bean class type
     * @param valueLoader value loader
     * @param <T>         type
     * @return value or null
     */
    <T> T get(Object key, Class<T> classType, Callable<T> valueLoader);

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
    default <T> void setEx(String key, T value, long time, TimeUnit timeUnit) {
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

    /**
     * 移除指定key的缓存
     *
     * @param key key
     * @return true if remove else not
     */
    boolean remove(String key);

    /**
     * 移除指定key的缓存
     *
     * @param keys key
     * @return true if remove else not
     */
    boolean remove(Collection<?> keys);
}
