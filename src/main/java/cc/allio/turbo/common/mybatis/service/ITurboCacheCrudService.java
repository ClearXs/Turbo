package cc.allio.turbo.common.mybatis.service;

import cc.allio.turbo.common.mybatis.entity.IdEntity;
import cc.allio.turbo.common.cache.CacheName;
import cc.allio.turbo.common.cache.TurboCache;

/**
 * 具有缓存 curd，包含如下特性
 * <ol>
 *
 * </ol>
 *
 * @author j.x
 * @date 2023/11/23 11:19
 * @since 0.1.0
 */
public interface ITurboCacheCrudService<T extends IdEntity> extends ITurboCrudService<T>, CacheName {

    /**
     * 获取缓存实例
     */
    TurboCache<T> getCache();
}
