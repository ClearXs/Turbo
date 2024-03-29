package cc.allio.turbo.common.cache;

/**
 * 定义CacheName
 *
 * @author j.x
 * @date 2024/1/28 18:42
 * @since 0.1.0
 */
@FunctionalInterface
public interface CacheName {

    /**
     * 获取cache name
     *
     * @return cache name for string
     */
    String getCacheName();
}
