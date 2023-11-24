package cc.allio.uno.turbo.common.mybatis.service;

import cc.allio.uno.core.datastructure.tree.DefaultExpand;
import cc.allio.uno.core.datastructure.tree.TreeSupport;
import cc.allio.uno.core.util.ClassUtils;
import cc.allio.uno.turbo.common.mybatis.entity.TreeEntity;
import cc.allio.uno.turbo.common.support.DomainTree;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * 树化与展开
 *
 * @author j.x
 * @date 2023/11/9 18:29
 * @since 1.0.0
 */
public interface ITurboTreeCrudService<T extends TreeEntity> extends ITurboCrudService<T> {

    /**
     * 平展树结构进行树化
     *
     * @param expands  平展结构
     * @param treeType 转换的树类型
     * @param <R>      平展结构类型
     * @param <Z>      树结构类型
     */
    default <Z extends DomainTree<Z, T>> List<Z> treeify(List<T> expands, Class<Z> treeType) {
        return TreeSupport.treeify(expands, e -> ClassUtils.newInstance(treeType, e));
    }

    /**
     * 树平展
     *
     * @param forest 树实例
     * @param <Z>
     */
    default <Z extends DomainTree<Z, DefaultExpand>> List<DefaultExpand> expand(List<Z> forest) {
        return TreeSupport.expand(forest);
    }

    /**
     * 树平展
     *
     * @param forest     树实例
     * @param expandFunc 转换
     * @param <Z>
     */
    default <Z extends DomainTree<Z, T>> List<T> expand(List<Z> forest, Function<Z, T> expandFunc) {
        return expand(forest, expandFunc, null);
    }

    /**
     * 树平展
     *
     * @param forest     树实例
     * @param expandFunc 转换
     * @param comparator 排序
     * @param <Z>        树类型
     * @param <R>        平展结构类型
     */
    default <Z extends DomainTree<Z, T>> List<T> expand(List<Z> forest, Function<Z, T> expandFunc, Comparator<T> comparator) {
        return TreeSupport.expand(forest, expandFunc, comparator);
    }
}
