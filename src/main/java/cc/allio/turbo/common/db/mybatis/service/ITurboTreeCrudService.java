package cc.allio.turbo.common.db.mybatis.service;

import cc.allio.turbo.common.domain.TreeDomain;
import cc.allio.turbo.common.db.entity.TreeNodeEntity;
import cc.allio.uno.core.datastructure.tree.TreeSupport;
import cc.allio.uno.core.util.ClassUtils;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * 关于树形额机构通用查询，包含如下特性：
 * <ol>
 *     <li>支持数据库级别的树查询</li>
 *     <li>支持进行树结构的构造{@link #treeify(List, Class)}</li>
 *     <li>支持删除对子结点的判断，必须要先删除子结点才能删除</li>
 * </ol>
 *
 * @author j.x
 * @date 2023/11/9 18:29
 * @since 0.1.0
 */
public interface ITurboTreeCrudService<T extends TreeNodeEntity> extends ITurboCrudService<T> {

    /**
     * 树查询
     *
     * @return 树展开结构
     */
    default List<T> tree() {
        return tree(Wrappers.emptyWrapper());
    }

    /**
     * 树查询
     *
     * @param queryWrapper 查询条件
     * @return 树展开结构
     */
    default List<T> tree(Wrapper<T> queryWrapper) {
        return tree(queryWrapper, true);
    }

    /**
     * 树查询
     *
     * @param queryWrapper 查询条件
     * @param recursive    weather recursive
     *
     * @return 树展开结构
     */
    List<T> tree(Wrapper<T> queryWrapper, Boolean recursive);

    /**
     * 树查询
     *
     * @param treeType 领域树类型
     * @param <Z>      领域树类型
     * @return 领域树结构
     */
    default <Z extends TreeDomain<T, Z>> List<Z> tree(Class<Z> treeType) {
        return tree(Wrappers.emptyWrapper(), treeType);
    }

    /**
     * 树查询
     *
     * @param <Z>          领域树类型
     * @param queryWrapper 查询条件
     * @param treeType     领域树类型
     * @return 领域树结构
     */
    default <Z extends TreeDomain<T, Z>> List<Z> tree(Wrapper<T> queryWrapper, Class<Z> treeType) {
        return tree(queryWrapper, treeType, true);
    }

    /**
     * 树查询
     *
     * @param <Z>          领域树类型
     * @param queryWrapper 查询条件
     * @param treeType     领域树类型
     * @param recursive    whether recursive
     * @return 领域树结构
     */
    <Z extends TreeDomain<T, Z>> List<Z> tree(Wrapper<T> queryWrapper, Class<Z> treeType, Boolean recursive);

    /**
     * 平展树结构进行树化
     *
     * @param expands  平展结构
     * @param treeType 转换的树类型
     * @param <Z>      领域树类型
     */
    default <Z extends TreeDomain<T, Z>> List<Z> treeify(List<T> expands, Class<Z> treeType) {
        return TreeSupport.treeify(expands, e -> ClassUtils.newInstance(treeType, e));
    }

    /**
     * 树平展
     *
     * @param forest     树实例
     * @param expandFunc 转换
     * @param <Z>        领域树类型
     */
    default <Z extends TreeDomain<T, Z>> List<T> expand(List<Z> forest, Function<Z, T> expandFunc) {
        return expand(forest, expandFunc, null);
    }

    /**
     * 树平展
     *
     * @param forest     树实例
     * @param expandFunc 转换
     * @param comparator 排序
     * @param <Z>        树类型
     */
    default <Z extends TreeDomain<T, Z>> List<T> expand(List<Z> forest, Function<Z, T> expandFunc, Comparator<T> comparator) {
        return TreeSupport.expand(forest, expandFunc, comparator);
    }
}
