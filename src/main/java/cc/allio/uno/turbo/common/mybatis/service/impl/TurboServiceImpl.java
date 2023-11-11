package cc.allio.uno.turbo.common.mybatis.service.impl;

import cc.allio.uno.core.datastructure.tree.Expand;
import cc.allio.uno.core.datastructure.tree.TreeSupport;
import cc.allio.uno.core.util.ClassUtils;
import cc.allio.uno.turbo.common.mybatis.service.ITurboService;
import cc.allio.uno.turbo.common.support.DomainTree;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * turbo service impl
 *
 * @author j.x
 * @date 2023/11/9 18:29
 * @since 1.0.0
 */
public abstract class TurboServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements ITurboService<T> {

    @Override
    public <Z extends DomainTree<R>, R extends Expand> List<Z> treeify(List<R> expands, Class<Z> treeType) {
        return TreeSupport.treeify(expands, e -> ClassUtils.newInstance(treeType, e));
    }

    @Override
    public <Z extends DomainTree<R>, R extends Expand> List<R> expand(List<Z> forest) {
        return TreeSupport.expand(forest);
    }
}
