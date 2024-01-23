package cc.allio.turbo.common.web;

import cc.allio.turbo.common.db.entity.Entity;
import cc.allio.turbo.common.db.mybatis.service.ITurboCrudService;

/**
 * 当使用{@link TurboCrudController}时，对其每一个方法进行拦截。
 * 临时性使用
 *
 * @param <T> 实体类型
 * @param <D> 领域类型
 * @param <S> 实体对应service类型
 * @author jiangwei
 * @date 2024/1/18 18:41
 * @since 0.1.0
 */
public interface WebCrudInterceptor<T extends Entity, D extends Entity, S extends ITurboCrudService<T>>
        extends WebCrudBeforeInterceptor<T, D, S>, WebCrudAfterInterceptor<T, D, S> {

}

