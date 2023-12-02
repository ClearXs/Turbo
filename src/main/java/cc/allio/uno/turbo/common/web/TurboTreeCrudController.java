package cc.allio.uno.turbo.common.web;

import cc.allio.uno.core.util.ReflectTool;
import cc.allio.uno.turbo.common.exception.BizException;
import cc.allio.uno.turbo.common.i18n.ExceptionCodes;
import cc.allio.uno.turbo.common.mybatis.help.Conditions;
import cc.allio.uno.turbo.common.mybatis.entity.TreeEntity;
import cc.allio.uno.turbo.common.web.params.QueryParam;
import cc.allio.uno.turbo.common.mybatis.service.ITurboTreeCrudService;
import cc.allio.uno.turbo.common.support.DomainTree;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public abstract class TurboTreeCrudController<Z extends DomainTree<Z, T>, T extends TreeEntity>
        extends TurboCrudController<T, ITurboTreeCrudService<T>, T> {

    @PostMapping("/tree")
    @Operation(summary = "树查询")
    public R<List<Z>> tree(@RequestBody QueryParam<T> params) throws BizException {
        Class<Z> treeType = getTreeType();
        if (treeType == null) {
            throw new BizException(ExceptionCodes.OPERATE_ERROR);
        }
        ITurboTreeCrudService<T> service = getService();
        QueryWrapper<T> queryWrapper = Conditions.query(params, getEntityType());
        List<Z> treeify = service.tree(queryWrapper, treeType);
        return ok(treeify);
    }

    @Override
    protected Class<T> getEntityType() {
        return (Class<T>) ReflectTool.getGenericType(this, TurboTreeCrudController.class, 1);
    }

    /**
     * 获取树类型
     */
    protected Class<Z> getTreeType() {
        return (Class<Z>) ReflectTool.getGenericType(this, TurboTreeCrudController.class, 0);
    }
}
