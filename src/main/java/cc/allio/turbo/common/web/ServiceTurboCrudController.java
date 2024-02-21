package cc.allio.turbo.common.web;

import cc.allio.turbo.common.db.entity.Entity;
import cc.allio.turbo.common.db.mybatis.service.ITurboCrudService;

/**
 * 子类需指定的Service类型
 *
 * @param <T> 实体类型
 * @param <S> Service类型
 * @author jiangwei
 * @date 2024/1/19 13:47
 * @since 0.1.0
 */
public abstract class ServiceTurboCrudController<T extends Entity, S extends ITurboCrudService<T>> extends TurboCrudController<T, T, S> {
}
