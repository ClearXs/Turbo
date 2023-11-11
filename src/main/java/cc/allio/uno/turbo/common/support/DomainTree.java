package cc.allio.uno.turbo.common.support;

import cc.allio.uno.core.datastructure.tree.DefaultElement;
import cc.allio.uno.core.datastructure.tree.Expand;
import org.springframework.beans.BeanUtils;

/**
 * 分层领域树模型，提供平展树结构转换为层次树结构
 *
 * @author j.x
 * @date 2023/11/9 15:36
 * @since 1.0.0
 */
public abstract class DomainTree<T extends Expand> extends DefaultElement {

    protected DomainTree(T expand) {
        super(expand.getId());
        BeanUtils.copyProperties(expand, this);
    }
}
