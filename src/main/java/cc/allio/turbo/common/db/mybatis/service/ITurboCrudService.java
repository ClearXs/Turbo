package cc.allio.turbo.common.db.mybatis.service;

import cc.allio.turbo.common.db.entity.Entity;
import cc.allio.turbo.common.domain.BehaviorSubscription;
import cc.allio.turbo.common.domain.MultiObservable;
import cc.allio.turbo.common.domain.Observable;
import cc.allio.turbo.common.domain.Subscriber;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * 拓展mybatis-plus service 功能
 *
 * @author j.x
 * @date 2023/11/22 16:05
 * @since 0.1.0
 */
public interface ITurboCrudService<T extends Entity> extends IService<T>, Subscriber<T> {

    /**
     * 实体的详情，详细返回的数据接口可能是原实体，可能是复合数据结构
     *
     * @param id  id
     * @param <V> 复合数据类型
     * @return 复合数据示例
     */
    <V extends T> V details(Serializable id);


    // ==================== subscription combination method ====================

    /**
     * combine all about 'insert' operator.
     * <ol>
     *     <li>{@link #save(Object)}</li>
     *     <li>{@link #saveBatch(Collection)}</li>
     *     <li>{@link #saveOrUpdate(Object)}</li>
     * </ol>
     *
     * @return the {@link MultiObservable} instance
     */
    default Observable<T> subscribeOnInsert() {
        return subscribeOnMultiple(
                subscribeOn("save"),
                subscribeOn("saveBatch"),
                subscribeOn("saveOrUpdate")
        );
    }

    /**
     * combine all about 'update' operator
     * <ol>
     *     <li>{@link #update()}</li>
     *     <li>{@link #updateBatchById(Collection)}</li>
     *     <li>{@link #updateById(Object)}</li>
     *     <li>{@link #saveOrUpdate(Object)}</li>
     * </ol>
     *
     * @return the {@link MultiObservable} instance
     */
    default Observable<T> subscribeOnUpdate() {
        return subscribeOnMultiple(
                subscribeOn("update"),
                subscribeOn("updateBatchById"),
                subscribeOn("updateById"),
                subscribeOn("saveOrUpdate")
        );
    }

    /**
     * combine all about 'delete' operator.
     *
     * <ol>
     *     <li>{@link #remove(Wrapper)}</li>
     *     <li>{@link #removeBatchByIds(Collection)}</li>
     *     <li>{@link #removeByIds(Collection)}</li>
     *     <li>{@link #removeByMap(Map)}</li>
     * </ol>
     *
     * @return the {@link MultiObservable} instance
     */
    default Observable<T> subscribeOnDelete() {
        return subscribeOnMultiple(
                subscribeOn("remove"),
                subscribeOn("removeBatchByIds"),
                subscribeOn("removeByIds"),
                subscribeOn("removeByMap")
        );
    }
}
