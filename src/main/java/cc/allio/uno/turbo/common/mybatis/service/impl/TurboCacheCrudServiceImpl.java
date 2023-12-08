package cc.allio.uno.turbo.common.mybatis.service.impl;

import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.turbo.common.cache.CacheHelper;
import cc.allio.uno.turbo.common.cache.TurboCache;
import cc.allio.uno.turbo.common.mybatis.entity.IdEntity;
import cc.allio.uno.turbo.common.mybatis.service.ITurboCacheCrudService;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 缓存实现。涉及以下方面改造
 * <ul>
 *     <li>查：先从缓存中查询，如果缓存没有在从数据库查询</li>
 *     <li>增：先缓存在数据库，保持数据一致性</li>
 *     <li>删：先缓存在数据库，保持数据一致性</li>
 *     <li>更：先缓存在数据库，保持数据一致性</li>
 * </ul>
 *
 * @author j.x
 * @date 2023/12/1 09:52
 * @since 0.1.0
 */
public abstract class TurboCacheCrudServiceImpl<M extends BaseMapper<T>, T extends IdEntity>
        extends TurboCrudServiceImpl<M, T> implements ITurboCacheCrudService<T> {

    @Override
    public TurboCache<T> getCache() {
        String cacheName = getCacheName();
        if (StringUtils.isBlank(cacheName)) {
            return null;
        }
        return CacheHelper.getCache(cacheName);
    }
}
