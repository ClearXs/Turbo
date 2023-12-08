package cc.allio.uno.turbo.common.support;

import cc.allio.uno.core.datastructure.tree.ComparableElement;
import cc.allio.uno.core.datastructure.tree.Expand;
import org.springframework.beans.BeanUtils;

import java.util.Comparator;

/**
 * 领域树模型，提供平展树结构转换为层次树结构
 *
 * @param <T> 自身树类型
 * @param <R> 平展结构类型
 * @author j.x
 * @date 2023/11/9 15:36
 * @since 0.1.0
 */
public abstract class DomainTree<T extends DomainTree<T, R>, R extends Expand> extends ComparableElement<T> {

    protected DomainTree(R expand, Comparator<T> comparator) {
        super(expand.getId(), comparator);
        BeanUtils.copyProperties(expand, this);
    }
}
