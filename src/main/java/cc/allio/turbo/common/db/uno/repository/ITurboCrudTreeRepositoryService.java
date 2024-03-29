package cc.allio.turbo.common.db.uno.repository;

import cc.allio.turbo.common.db.entity.TreeNodeEntity;
import cc.allio.turbo.common.db.mybatis.service.ITurboTreeCrudService;
import cc.allio.turbo.common.domain.TreeDomain;
import cc.allio.uno.core.exception.Exceptions;
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
        throw Exceptions.unOperate("tree");
    }

    @Override
    default <Z extends TreeDomain<T, Z>> List<Z> tree(Wrapper<T> queryWrapper, Class<Z> treeType) {
        throw Exceptions.unOperate("tree");
    }
}
