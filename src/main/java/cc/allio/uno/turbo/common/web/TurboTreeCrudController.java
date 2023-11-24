package cc.allio.uno.turbo.common.web;

import cc.allio.uno.turbo.common.exception.BizException;
import cc.allio.uno.turbo.common.i18n.ExceptionCodes;
import cc.allio.uno.turbo.common.mybatis.help.Conditions;
import cc.allio.uno.turbo.common.mybatis.entity.TreeEntity;
import cc.allio.uno.turbo.common.mybatis.params.GeneralParams;
import cc.allio.uno.turbo.common.mybatis.service.ITurboTreeCrudService;
import cc.allio.uno.turbo.common.support.DomainTree;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public abstract class TurboTreeCrudController<Z extends DomainTree<Z, T>, T extends TreeEntity>
        extends TurboCrudController<T, ITurboTreeCrudService<T>> {

    @GetMapping("/tree")
    @Operation(summary = "树")
    public R<List<Z>> tree(GeneralParams<T> params) throws BizException {
        ITurboTreeCrudService<T> service = getService();
        QueryWrapper<T> queryWrapper = Conditions.query(params);
        List<T> list = service.list(queryWrapper);
        Class<Z> treeType = getTreeType();
        if (treeType == null) {
            throw new BizException(ExceptionCodes.OPERATE_ERROR);
        }
        List<Z> treeify = service.treeify(list, treeType);
        return ok(treeify);
    }

    /**
     * 获取树类型
     */
    protected abstract Class<Z> getTreeType();
}
