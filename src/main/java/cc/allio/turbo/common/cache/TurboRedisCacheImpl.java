package cc.allio.turbo.common.cache;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.type.Types;
import cc.allio.uno.core.util.JsonUtils;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.core.util.template.ExpressionTemplate;
import cc.allio.uno.core.util.template.Tokenizer;
import cc.allio.turbo.common.util.RedisUtil;
import lombok.Getter;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 基于redis缓存
 * <p>key值将使用{@link ConversionService}转换为{@link String}类型，并且自动添加前缀的信息，如果存在租户则添加租户的信息</p>
 *
 * @author j.x
 * @date 2023/10/27 14:57
 * @see org.springframework.data.redis.cache.RedisCache
 * @since 0.1.0
 */
@Getter
public class TurboRedisCacheImpl implements TenantCache {

    // 默认key前缀模版
    private static final String PREFIX_TEMPLATE = "turbo:#{name}:";
    // 带有租户的key前缀
    private static final String PREFIX_TENANT_TEMPLATE = "turbo:#{name}:#{tenant}:";
    private static final ExpressionTemplate expressionTemplate = ExpressionTemplate.createTemplate(Tokenizer.HASH_BRACE);

    private final ConversionService conversionService;
    private final String name;

    public TurboRedisCacheImpl(String name) {
        this(name, new DefaultConversionService());
    }

    protected TurboRedisCacheImpl(String name, ConversionService conversionService) {
        this.conversionService = conversionService;
        this.name = name;
    }

    @Override
    public boolean hasKey(String key) {
        String cacheKey = createCacheKey(key);
        return RedisUtil.hasKey(cacheKey);
    }

    @Override
    public void setEx(String key, ValueWrapper value, long time, TimeUnit timeUnit) {
        String cacheKey = createCacheKey(key);
        RedisUtil.setEx(cacheKey, JsonUtils.toJson(value.get()), time, timeUnit);
    }

    @Override
    public Long getExpire(String key, TimeUnit unit) {
        String cacheKey = createCacheKey(key);
        return RedisUtil.getExpire(cacheKey);
    }

    @Override
    public boolean remove(String key) {
        String cacheKey = createCacheKey(key);
        RedisUtil.delete(cacheKey);
        return true;
    }

    @Override
    public boolean remove(Collection<?> keys) {
        List<String> cacheKeys = keys.stream().map(this::createCacheKey).toList();
        RedisUtil.delete(cacheKeys);
        return true;
    }

    @Override
    public Object getNativeCache() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ValueWrapper get(Object key) {
        String cacheKey = createCacheKey(key);
        return () -> RedisUtil.get(cacheKey);
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        String cacheKey = createCacheKey(key);
        return JsonUtils.parse(RedisUtil.get(cacheKey), type);
    }


    @Override
    public <T> T get(Object key, Class<T> classType, Callable<T> valueLoader) {
        String cacheKey = createCacheKey(key);
        String cacheValue = RedisUtil.get(cacheKey);
        if (StringUtils.isBlank(cacheValue)) {
            T value = null;
            try {
                value = valueLoader.call();
            } catch (Exception ex) {
                throw new ValueRetrievalException(key, valueLoader, ex);
            }
            if (value != null) {
                RedisUtil.setIfAbsent(cacheKey, JsonUtils.toJson(value));
            }
            return value;
        } else {
            return JsonUtils.parse(cacheValue, classType);
        }
    }

    @Override
    public void put(Object key, Object value) {
        String cacheKey = createCacheKey(key);
        RedisUtil.set(cacheKey, JsonUtils.toJson(value));
    }

    @Override
    public void evict(Object key) {
        String cacheKey = createCacheKey(key);
        RedisUtil.delete(cacheKey);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("remove not support");
    }

    /**
     * 识别给定参数{@link Object}类型的key，可以自定义{@link ConversionService}转换，最终添加prefix
     *
     * @param key the object key
     * @return never null
     */
    private String createCacheKey(Object key) {
        return prefixCacheKey(convertKey(key));
    }

    /**
     * Convert {@code key} to a {@link String} representation used for cache key creation.
     *
     * @param key will never be {@literal null}.
     * @return never {@literal null}.
     * @throws IllegalStateException if {@code key} cannot be converted to {@link String}.
     */
    protected String convertKey(Object key) {
        if (key instanceof String keyString) {
            return keyString;
        }
        TypeDescriptor source = TypeDescriptor.forObject(key);
        ConversionService conversionService = getConversionService();
        if (conversionService.canConvert(source, TypeDescriptor.valueOf(String.class))) {
            try {
                return conversionService.convert(key, String.class);
            } catch (ConversionFailedException cause) {

                // May fail if the given key inputStream a collection
                if (Types.isList(source.getType()) || Types.isMap(source.getType())) {
                    return convertCollectionLikeOrMapKey(key, source);
                }
                throw cause;
            }
        }
        if (hasToStringMethod(key)) {
            return key.toString();
        }

        String message = String.format("Cannot convert cache key %s to String; Please register a suitable Converter"
                        + " via 'RedisCacheConfiguration.configureKeyConverters(...)' or override '%s.toString()'",
                source, key.getClass().getName());
        throw new IllegalStateException(message);
    }

    private String convertCollectionLikeOrMapKey(Object key, TypeDescriptor source) {

        if (source.isMap()) {

            int count = 0;

            StringBuilder target = new StringBuilder("{");

            for (Map.Entry<?, ?> entry : ((Map<?, ?>) key).entrySet()) {
                target.append(convertKey(entry.getKey())).append("=").append(convertKey(entry.getValue()));
                target.append(++count > 1 ? ", " : "");
            }

            target.append("}");

            return target.toString();
        } else if (source.isCollection() || source.isArray()) {

            StringJoiner stringJoiner = new StringJoiner(",");

            Collection<?> collection = source.isCollection() ? (Collection<?>) key
                    : Arrays.asList(ObjectUtils.toObjectArray(key));

            for (Object collectedKey : collection) {
                stringJoiner.add(convertKey(collectedKey));
            }

            return "[" + stringJoiner + "]";
        }

        throw new IllegalArgumentException(String.format("Cannot convert cache key [%s] to String", key));
    }

    private boolean hasToStringMethod(Object target) {
        return hasToStringMethod(target.getClass());
    }

    private boolean hasToStringMethod(Class<?> type) {

        Method toString = ReflectionUtils.findMethod(type, "toString");

        return toString != null && !Object.class.equals(toString.getDeclaringClass());
    }

    /**
     * 获取key前缀
     */
    private String prefixCacheKey(String key) {
        String paramKey = Optional.ofNullable(key).orElse(StringPool.EMPTY);
        String tenant = getTenant();
        if (StringUtils.isBlank(tenant)) {
            return expressionTemplate.parseTemplate(PREFIX_TEMPLATE, "name", getName()).concat(paramKey);
        } else {
            return expressionTemplate.parseTemplate(PREFIX_TENANT_TEMPLATE, "name", getName(), "tenant", tenant).concat(paramKey);
        }
    }
}
