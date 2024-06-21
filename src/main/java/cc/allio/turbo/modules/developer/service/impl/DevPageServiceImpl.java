package cc.allio.turbo.modules.developer.service.impl;

import cc.allio.turbo.common.db.mybatis.service.impl.TurboCrudServiceImpl;
import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl;
import cc.allio.turbo.common.db.uno.repository.impl.TurboCrudRepositoryServiceImpl;
import cc.allio.turbo.modules.developer.entity.DevBo;
import cc.allio.turbo.modules.developer.entity.DevForm;
import cc.allio.turbo.modules.developer.entity.DevPage;
import cc.allio.turbo.modules.developer.mapper.DevPageMapper;
import cc.allio.turbo.modules.developer.service.IDevBoService;
import cc.allio.turbo.modules.developer.service.IDevFormService;
import cc.allio.turbo.modules.developer.service.IDevPageService;
import cc.allio.turbo.modules.system.constant.MenuType;
import cc.allio.turbo.modules.system.entity.SysMenu;
import cc.allio.turbo.modules.system.service.ISysMenuService;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.core.util.template.ExpressionTemplate;
import cc.allio.uno.core.util.template.Tokenizer;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.util.function.Tuples;

@Service
@AllArgsConstructor
public class DevPageServiceImpl extends SimpleTurboCrudRepositoryServiceImpl<DevPage> implements IDevPageService {

    private final IDevFormService formService;
    private final IDevBoService boService;
    private final ISysMenuService menuService;

    private static final String DOMAIN_PAGE_TEMPLATE = "/domain/#{pageId}";
    private static final ExpressionTemplate TEMPLATE_PARSER = ExpressionTemplate.createTemplate(Tokenizer.HASH_BRACE);

    @Override
    @Transactional
    public boolean save(DevPage entity) {
        // 如果未为绑定bo
        Long boId = entity.getBoId();
        if (boId == null) {
            DevBo bo = new DevBo();
            bo.setName(entity.getName());
            bo.setCode(entity.getCode());
            boService.save(bo);
            entity.setBoId(bo.getId());
        }
        Long formId = entity.getFormId();
        // 如果page未绑定form，则基于当前page信息创建新的form
        if (formId == null) {
            DevForm form = new DevForm();
            form.setName(entity.getName());
            form.setCode(entity.getCode());
            form.setBoId(entity.getBoId());
            formService.save(form);
            entity.setFormId(form.getId());
        }
        return super.save(entity);
    }

    @Override
    @Transactional
    public boolean deploy(DevPage devPage, Long menuId) {
        String route = devPage.getRoute();
        String menuRoute = TEMPLATE_PARSER.parseTemplate(DOMAIN_PAGE_TEMPLATE, Tuples.of("pageId", devPage.getId()));
        if (StringUtils.isNotBlank(route)) {
            // 更新旧的菜单项
            return menuService.update(Wrappers.<SysMenu>lambdaUpdate().set(SysMenu::getParentId, menuId).eq(SysMenu::getId, Long.valueOf(route)));
        } else {
            SysMenu sysMenu = new SysMenu();
            sysMenu.setName(devPage.getName());
            sysMenu.setCode(devPage.getCode());
            sysMenu.setType(MenuType.PAGE);
            sysMenu.setRoute(menuRoute);
            sysMenu.setParentId(menuId);
            menuService.save(sysMenu);
            devPage.setRoute(String.valueOf(sysMenu.getId()));
        }
        return updateById(devPage);
    }
}
