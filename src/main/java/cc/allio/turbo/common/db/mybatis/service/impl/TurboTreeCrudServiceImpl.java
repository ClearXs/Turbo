package cc.allio.turbo.common.db.mybatis.service.impl;

import cc.allio.turbo.common.domain.TreeDomain;
import cc.allio.turbo.common.db.entity.TreeEntity;
import cc.allio.turbo.common.db.mybatis.mapper.TreeMapper;
import cc.allio.turbo.common.db.mybatis.service.ITurboTreeCrudService;
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
 * @since 0.1.0
 */
public abstract class TurboTreeCrudServiceImpl<M extends TreeMapper<T>, T extends TreeEntity>
        extends TurboCrudServiceImpl<M, T>
        implements ITurboTreeCrudService<T> {

    @Override
    public List<T> tree(Wrapper<T> queryWrapper, Boolean recursive) {
        if (Boolean.TRUE.equals(recursive)) {
            return getBaseMapper().selectTree(queryWrapper);
        } else {
            return getBaseMapper().selectNonRecursiveTree(queryWrapper);
        }
    }

    @Override
    public <Z extends TreeDomain<T, Z>> List<Z> tree(Wrapper<T> queryWrapper, Class<Z> treeType, Boolean recursive) {
        List<T> tree = this.tree(queryWrapper, recursive);
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
        if (id != null) {
            QueryWrapper<T> queryWrapper = Wrappers.query();
            queryWrapper.eq("parent_id", id);
            queryWrapper.ne("id", id);
            Long childrenCount = getBaseMapper().selectCount(queryWrapper);
            if (childrenCount > 0) {
                throw ExceptionUtils.mpe(String.format("parent_id %s has children", id));
            }
        }
    }
}
