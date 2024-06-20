package cc.allio.turbo.extension.swift;

import cc.allio.uno.core.util.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;

/**
 * 流水号生成相关操作
 *
 * @author j.x
 * @date 2024/4/23 13:51
 * @since 0.0.1
 */
public class Swift {

    private final StringRedisTemplate redisTemplate;

    private static final String SWIFT_CACHE_PREFIX = "turbo:sequence:increment:";

    public Swift(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * reset auto number
     *
     * @param key the swift key
     * @return true if success
     */
    public Boolean reset(SwiftConfig swiftConfig) {
        String cacheKey = swiftConfig.getCacheKey();
        String key = cacheKey + swiftConfig.getAlias();
        String incrementKey = SWIFT_CACHE_PREFIX + key;
        return redisTemplate.delete(incrementKey);
    }

    /**
     * from cache get current sequence number
     *
     * @param key the key
     * @return the sequence no
     */
    public String getNo(String key) {
        String incrementKey = SWIFT_CACHE_PREFIX + key;
        Object no = redisTemplate.opsForValue().get(incrementKey);
        if (no != null) {
            return no.toString();
        }
        return StringPool.EMPTY;
    }

    /**
     * 生成每日流水号。格式为：[prefix][yyMMdd][当日流水（长度为numLen）]
     *
     * @param prefix the prefix key
     * @param numLen the sequence length
     * @param step   the generative step
     * @return String
     */
    public String makeDay(String prefix, int numLen, int step) {
        StringBuilder result = new StringBuilder(prefix);
        String ymd = DateUtil.getNowYMD();
        result.append(ymd);
        long id = makeId(result.toString(), 1, step, Duration.ofDays(2), ChangeType.INCR);
        int size = String.valueOf(id).length();
        if (size < numLen) {
            result.append("0".repeat(numLen - size));
        }
        result.append(id);
        return result.toString();
    }

    /**
     * 生成每月流水号。格式为：[prefix][yyMM][当日流水（长度为numLen）]
     *
     * @param prefix the prefix key
     * @param numLen the sequence length
     * @param step   the generative step
     * @return String
     */
    public String makeMonth(String prefix, int numLen, int step) {
        StringBuilder result = new StringBuilder(prefix);
        String ym = DateUtil.getNowYM();
        result.append(ym);
        long id = makeId(result.toString(), 1, step, Duration.ofDays(32), ChangeType.INCR);
        int size = String.valueOf(id).length();
        if (size < numLen) {
            result.append("0".repeat(numLen - size));
        }
        result.append(id);
        return result.toString();
    }

    /**
     * 生成每月流水号。格式为：[prefix][yy][当日流水（长度为numLen）]
     *
     * @param prefix the prefix key
     * @param numLen the sequence length
     * @param step   the generative step
     * @return String
     */
    public String makeYear(String prefix, int numLen, int step) {
        StringBuilder result = new StringBuilder(prefix);
        String year = DateUtil.getNowY();
        result.append(year);
        long id = makeId(result.toString(), 1, step, Duration.ofDays(367), ChangeType.INCR);
        int size = String.valueOf(id).length();
        if (size < numLen) {
            result.append("0".repeat(numLen - size));
        }
        result.append(id);
        return result.toString();
    }

    /**
     * yyyy: 年，MM: 月（补零），mm：月（不补零），DD：日（补零），dd：日（不补零），NO，补零，no，不补零。
     *
     * @return
     */
    public String make(SwiftConfig swiftConfig) {
        if (swiftConfig.isDataPass()) {
            String cacheKey = swiftConfig.getCacheKey();
            Duration cacheDays = swiftConfig.getCacheDays();
            long id = makeId(
                    cacheKey + swiftConfig.getAlias(),
                    swiftConfig.getInitNumber(),
                    swiftConfig.getStep(),
                    cacheDays,
                    swiftConfig.getChangeType());
            swiftConfig.setIdToParams(String.valueOf(id));
            return swiftConfig.getExtend();
        }
        return "";
    }

    /**
     * make next no from key and step. and set expired sequence key
     *
     * @param key the sequence key
     * @param minValue the min value
     * @param step the generate step
     * @param expire the key expired time
     * @param changeType the change type
     * @return new number
     */
    Long makeId(String key, int minValue, int step, Duration expire, ChangeType changeType) {
        String incrementKey = SWIFT_CACHE_PREFIX + key;
        long id;
        synchronized (this) {
            if (expire != null) {
                redisTemplate.opsForValue().setIfAbsent(incrementKey, String.valueOf(minValue), expire);
            }
            id = switch (changeType) {
                case INCR -> redisTemplate.opsForValue().increment(incrementKey, step);
                case DECR -> redisTemplate.opsForValue().decrement(incrementKey, step);
                default -> redisTemplate.opsForValue().increment(incrementKey, 0);
            };
            if (id == 0) {
                id = minValue;
            } else if (id == step) {
                id = minValue;
            } else {
                id = minValue + id - step;
            }
            if ((id + 99) >= Long.MAX_VALUE || id < minValue) {
                redisTemplate.opsForValue().getAndSet(incrementKey, String.valueOf(minValue));
                id = minValue;
            }
        }
        return id;
    }
}
