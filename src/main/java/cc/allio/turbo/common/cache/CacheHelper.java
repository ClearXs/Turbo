package cc.allio.turbo.common.cache;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * cache工具类
 *
 * @author j.x
 * @date 2023/10/27 15:56
 * @since 0.1.0
 */
public class CacheHelper implements ApplicationContextAware {

    // 记录缓存名称
    public static final String CAPTCHA = "captcha";

    private static TurboCacheManager cacheManager;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        CacheHelper.cacheManager = applicationContext.getBean(TurboCacheManager.class);
    }

    /**
     * 根据缓存名称获取缓存示例
     *
     * @param cacheName cacheName
     * @return TurboCache instance
     */
    public static TurboCache getCache(String cacheName) {
        return (TurboCache) cacheManager.getCache(cacheName);
    }

    /**
     * 如果没有就创建默认的cache
     *
     * @param cacheName cacheName must be unique
     * @return TurboCache
     */
    public static TurboCache getIfAbsent(String cacheName) {
        return cacheManager.getIfAbsent(cacheName);
    }
}
