package cc.allio.turbo.common.db.uno.repository;

import cc.allio.turbo.common.db.entity.TreeNodeEntity;
import cc.allio.turbo.common.db.mybatis.service.ITurboTreeCrudService;
import cc.allio.turbo.common.db.uno.repository.mybatis.WrapperAdapter;
import cc.allio.turbo.common.domain.TreeDomain;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;

import java.util.List;

/**
 * 桥接的mybatis-plus tree实现
 *
 * @author j.x
 * @date 2024/2/6 15:04
 * @since 0.1.0
 */
public interface ITurboCrudTreeRepositoryService<T extends TreeNodeEntity> extends ITurboCrudRepositoryService<T>, ITurboTreeCrudService<T> {

    @Override
    default List<T> tree(Wrapper<T> queryWrapper) {
        Class<T> entityClass = getEntityClass();
        if (queryWrapper instanceof AbstractWrapper abstractWrapper) {
            abstractWrapper.setEntityClass(entityClass);
        }
        return getExecutor()
                .queryList(
                        entityClass,
                        o -> {
                            QueryOperator baseQuery = WrapperAdapter.adapt(queryWrapper, getExecutor().getOperatorGroup().query());
                            QueryOperator newQuery = getExecutor().getOperatorGroup().query();
                            QueryOperator subQuery = newQuery.select(entityClass).from(entityClass);
                            return o.tree(baseQuery, subQuery);
                        });
    }

    @Override
    default <Z extends TreeDomain<T, Z>> List<Z> tree(Wrapper<T> queryWrapper, Class<Z> treeType) {
        List<T> tree = tree(queryWrapper);
        return treeify(tree, treeType);
    }
}
