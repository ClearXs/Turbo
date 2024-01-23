package cc.allio.turbo.common.db.mybatis.service;

import cc.allio.turbo.common.cache.CacheHelper;
import cc.allio.turbo.common.db.entity.IdEntity;
import cc.allio.turbo.common.cache.CacheName;
import cc.allio.turbo.common.cache.TurboCache;
import cc.allio.uno.core.util.StringUtils;

/**
 * 具有缓存 curd。
 *
 * @author j.x
 * @date 2023/11/23 11:19
 * @since 0.1.0
 */
public interface ITurboCacheCrudService<T extends IdEntity> extends ITurboCrudService<T>, CacheName {

    /**
     * 获取缓存实例
     */
    default TurboCache<T> getCache() {
        String cacheName = getCacheName();
        if (StringUtils.isBlank(cacheName)) {
            return null;
        }
        return CacheHelper.getCache(cacheName);
    }
}
