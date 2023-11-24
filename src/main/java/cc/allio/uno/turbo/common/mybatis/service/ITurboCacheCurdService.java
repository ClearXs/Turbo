package cc.allio.uno.turbo.common.mybatis.service;

import cc.allio.uno.turbo.common.mybatis.entity.IdEntity;

/**
 * 具有缓存 curd，包含如下特性
 * <ol>
 *
 * </ol>
 *
 * @author j.x
 * @date 2023/11/23 11:19
 * @since 1.0.0
 */
public interface ITurboCacheCurdService<T extends IdEntity> extends ITurboCrudService<T> {
}
