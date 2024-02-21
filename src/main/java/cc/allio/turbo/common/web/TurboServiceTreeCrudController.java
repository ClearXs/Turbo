package cc.allio.turbo.common.web;

import cc.allio.turbo.common.db.mybatis.helper.Conditions;
import cc.allio.turbo.common.domain.TreeDomain;
import cc.allio.turbo.common.db.entity.TreeEntity;
import cc.allio.turbo.common.db.mybatis.service.ITurboTreeCrudService;
import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.common.i18n.ExceptionCodes;
import cc.allio.turbo.common.web.params.QueryParam;
import cc.allio.uno.core.util.ReflectTool;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public abstract class TurboServiceTreeCrudController<T extends TreeEntity, Z extends TreeDomain<T, Z>, S extends ITurboTreeCrudService<T>>
        extends TurboCrudController<T, Z, S> {

    @PostMapping("/tree")
    @Operation(summary = "树查询")
    public R<List<Z>> tree(@RequestBody QueryParam<T> params) throws BizException {
        WebTreeCrudInterceptor<T, Z, S> interceptor = getInterceptor();
        interceptor.onTreeBefore(getService(), params);
        Class<Z> treeType = getTreeType();
        if (treeType == null) {
            throw new BizException(ExceptionCodes.OPERATE_ERROR);
        }
        ITurboTreeCrudService<T> service = getService();
        QueryWrapper<T> queryWrapper = Conditions.entityQuery(params, getEntityType());
        List<Z> treeify = service.tree(queryWrapper, treeType);
        List<Z> zs = interceptor.onTreeAfter(getService(), treeify);
        return ok(zs);
    }

    @Override
    public Class<T> getEntityType() {
        return (Class<T>) ReflectTool.getGenericType(this, TurboServiceTreeCrudController.class, 0);
    }

    /**
     * 获取树类型
     */
    public Class<Z> getTreeType() {
        return (Class<Z>) ReflectTool.getGenericType(this, TurboServiceTreeCrudController.class, 1);
    }

    @Override
    public WebTreeCrudInterceptor<T, Z, S> getInterceptor() {
        return new WebTreeCrudInterceptor<>() {
        };
    }
}
