package cc.allio.uno.turbo.common.mybatis.service.impl;

import cc.allio.uno.turbo.common.mybatis.entity.TreeEntity;
import cc.allio.uno.turbo.common.mybatis.mapper.TreeMapper;
import cc.allio.uno.turbo.common.mybatis.service.ITurboTreeCrudService;
import cc.allio.uno.turbo.common.support.DomainTree;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * turbo tree service impl
 *
 * @author j.x
 * @date 2023/11/9 18:29
 * @since 1.0.0
 */
public abstract class TurboTreeCrudServiceImpl<M extends TreeMapper<T>, T extends TreeEntity>
        extends TurboCrudServiceImpl<M, T>
        implements ITurboTreeCrudService<T> {

    @Override
    public List<T> tree(Wrapper<T> queryWrapper) {
        return getBaseMapper().selectTree(queryWrapper);
    }

    @Override
    public <Z extends DomainTree<Z, T>> List<Z> tree(Wrapper<T> queryWrapper, Class<Z> treeType) {
        List<T> tree = getBaseMapper().selectTree(queryWrapper);
        return treeify(tree, treeType);
    }

    @Override
    public boolean removeById(Serializable id) {
        preCheckRemove(id);
        return super.removeById(id);
    }

    @Override
    public boolean removeByIds(Collection<?> list) {
        for (Object id : list) {
            preCheckRemove((Serializable) id);
        }
        return super.removeByIds(list);
    }

    @Override
    public boolean removeById(Serializable id, boolean useFill) {
        preCheckRemove(id);
        return super.removeById(id, useFill);
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list, int batchSize) {
        for (Object id : list) {
            preCheckRemove((Serializable) id);
        }
        return super.removeBatchByIds(list, batchSize);
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list, int batchSize, boolean useFill) {
        for (Object id : list) {
            preCheckRemove((Serializable) id);
        }
        return super.removeBatchByIds(list, batchSize, useFill);
    }

    /**
     * 移除前检查
     */
    private void preCheckRemove(Serializable id) {
        QueryWrapper<T> queryWrapper = Wrappers.query();
        queryWrapper.eq(id != null, "parent_id", id);
        Long childrenCount = getBaseMapper().selectCount(queryWrapper);
        if (childrenCount > 0) {
            throw ExceptionUtils.mpe(String.format("parent_id %s has children", id));
        }
    }
}
