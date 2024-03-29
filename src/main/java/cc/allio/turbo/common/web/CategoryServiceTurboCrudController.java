package cc.allio.turbo.common.web;

import cc.allio.turbo.common.db.entity.CategoryEntity;
import cc.allio.turbo.common.db.entity.IdEntity;
import cc.allio.turbo.common.db.mybatis.service.ITurboCrudService;

/**
 * category标识接口，子类需要指定service
 *
 * @param <T> 实体类型
 * @param <S> Service类型
 * @author j.x
 * @date 2024/1/19 13:48
 * @since 0.1.0
 */
public abstract class CategoryServiceTurboCrudController<T extends IdEntity & CategoryEntity, S extends ITurboCrudService<T>> extends TurboCrudController<T, T, S> {
}
