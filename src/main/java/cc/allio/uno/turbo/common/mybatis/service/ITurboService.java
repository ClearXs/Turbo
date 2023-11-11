package cc.allio.uno.turbo.common.mybatis.service;

import cc.allio.uno.core.datastructure.tree.Expand;
import cc.allio.uno.turbo.common.support.DomainTree;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 拓展mybatis-plus快速功能
 * <ul>
 *     <li>树化与展开</li>
 * </ul>
 *
 * @author j.x
 * @date 2023/11/9 18:29
 * @since 1.0.0
 */
public interface ITurboService<T> extends IService<T> {

    /**
     * 平展树形结构
     */
    <Z extends DomainTree<R>, R extends Expand> List<Z> treeify(List<R> expands, Class<Z> treeType);

    /**
     * 树平展
     */
    <Z extends DomainTree<R>, R extends Expand> List<R> expand(List<Z> forest);
}
