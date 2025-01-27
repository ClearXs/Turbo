package cc.allio.turbo.modules.development.controller;

import cc.allio.turbo.common.db.mybatis.helper.Conditions;
import cc.allio.turbo.common.db.mybatis.service.ITurboTreeCrudService;
import cc.allio.turbo.common.domain.JsonDomain;
import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.common.i18n.ExceptionCodes;
import cc.allio.turbo.common.web.R;
import cc.allio.turbo.common.web.TurboServiceTreeCrudController;
import cc.allio.turbo.common.web.WebTreeCrudInterceptor;
import cc.allio.turbo.common.web.params.QueryParam;
import cc.allio.turbo.modules.development.domain.BoAttributeTree;
import cc.allio.turbo.modules.development.domain.DevAttributeProps;
import cc.allio.turbo.modules.development.entity.DevBoAttribute;
import cc.allio.turbo.modules.development.service.IDevBoAttributeService;
import cc.allio.uno.core.util.BeanUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dev/bo/attribute")
@AllArgsConstructor
@Tag(name = "业务对象管理")
public class DevBoAttributeController extends TurboServiceTreeCrudController<DevBoAttribute, BoAttributeTree, IDevBoAttributeService> {

    private static final BoAttributeTreeInterceptor ATTRIBUTE_TREE_INTERCEPTOR = new BoAttributeTreeInterceptor();

    @Override
    public R<List<BoAttributeTree>> tree(QueryParam<DevBoAttribute> params) throws BizException {
        WebTreeCrudInterceptor<DevBoAttribute, BoAttributeTree, IDevBoAttributeService> interceptor = getInterceptor();
        interceptor.onTreeBefore(getService(), params);
        Class<BoAttributeTree> treeType = getTreeType();
        if (treeType == null) {
            throw new BizException(ExceptionCodes.OPERATE_ERROR);
        }
        ITurboTreeCrudService<DevBoAttribute> service = getService();
        QueryWrapper<DevBoAttribute> queryWrapper = Conditions.entityQuery(params, getEntityType());
        List<BoAttributeTree> treeify = service.tree(queryWrapper, treeType, false);
        List<BoAttributeTree> zs = interceptor.onTreeAfter(getService(), treeify);
        return ok(zs);
    }

    @Override
    public WebTreeCrudInterceptor<DevBoAttribute, BoAttributeTree, IDevBoAttributeService> getInterceptor() {
        return ATTRIBUTE_TREE_INTERCEPTOR;
    }

    public static class BoAttributeTreeInterceptor implements WebTreeCrudInterceptor<DevBoAttribute, BoAttributeTree, IDevBoAttributeService> {
        @Override
        public List<BoAttributeTree> onTreeAfter(IDevBoAttributeService service, List<BoAttributeTree> treeify) {
            for (BoAttributeTree tree : treeify) {
                String props = tree.getEntity().getProps();
                DevAttributeProps attributeProps = JsonDomain.from(props, DevAttributeProps.class);
                BeanUtils.copyProperties(attributeProps, treeify);
            }
            return treeify;
        }

        @Override
        public DevBoAttribute onEditBefore(IDevBoAttributeService service, BoAttributeTree domain) {
            return service.domainToEntity(domain);
        }

        @Override
        public DevBoAttribute onSaveBefore(IDevBoAttributeService service, BoAttributeTree domain) {
            return service.domainToEntity(domain);
        }

        @Override
        public DevBoAttribute onSaveOrUpdateBefore(IDevBoAttributeService service, BoAttributeTree domain) {
            return service.domainToEntity(domain);
        }

        @Override
        public List<DevBoAttribute> onBatchSaveBefore(IDevBoAttributeService service, List<BoAttributeTree> domains) {
            return domains.stream().map(service::domainToEntity).toList();
        }

        @Override
        public BoAttributeTree onDetailsAfter(IDevBoAttributeService service, DevBoAttribute entity) {
            return service.entityToDomain(entity);
        }

        @Override
        public List<BoAttributeTree> onListAfter(IDevBoAttributeService service, List<DevBoAttribute> entity, QueryParam<DevBoAttribute> params) {
            return entity.stream().map(service::entityToDomain).toList();
        }

        @Override
        public IPage<BoAttributeTree> onPageAfter(IDevBoAttributeService service, IPage<DevBoAttribute> entity, QueryParam<DevBoAttribute> params) {
            List<DevBoAttribute> records = entity.getRecords();
            List<BoAttributeTree> domains = records.stream().map(service::entityToDomain).toList();
            Page<BoAttributeTree> domainPage = Page.of(entity.getCurrent(), entity.getSize(), entity.getTotal());
            domainPage.setRecords(domains);
            return domainPage;
        }

    }
}
