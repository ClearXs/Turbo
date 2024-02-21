package cc.allio.turbo.common.web;

import cc.allio.turbo.common.domain.TreeDomain;
import cc.allio.turbo.common.db.entity.TreeEntity;
import cc.allio.turbo.common.db.entity.TreeNodeEntity;
import cc.allio.turbo.common.db.mybatis.service.ITurboTreeCrudService;
import cc.allio.turbo.common.web.params.QueryParam;

import java.util.List;

/**
 * 当使用{@link TurboTreeCrudController}时，对其每一个方法进行拦截。
 *
 * @param <T> 实体类型
 * @param <D> 领域类型
 * @param <Z> 树结点
 * @author jiangwei
 * @date 2024/1/18 18:41
 * @since 0.1.0
 */
public interface WebTreeCrudInterceptor<T extends TreeEntity, Z extends TreeDomain<T, Z> & TreeNodeEntity, S extends ITurboTreeCrudService<T>> extends WebCrudInterceptor<T, Z, S> {

    /**
     * 在{@link TurboTreeCrudController#tree(QueryParam)}之前进行调用
     *
     * @param service entity service
     * @param params  params
     */
    default void onTreeBefore(S service, QueryParam<T> params) {
    }

    /**
     * 在{@link TurboTreeCrudController#tree(QueryParam)}之前进行调用
     *
     * @param service entity service
     * @param treeify treeify
     */
    default List<Z> onTreeAfter(S service, List<Z> treeify) {
        return treeify;
    }
}
