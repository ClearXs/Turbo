package cc.allio.turbo.modules.developer.controller;

import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.common.i18n.DevCodes;
import cc.allio.turbo.common.web.CategoryServiceTurboCrudController;
import cc.allio.turbo.common.web.R;
import cc.allio.turbo.modules.developer.domain.PageView;
import cc.allio.turbo.modules.developer.domain.view.DataView;
import cc.allio.turbo.modules.developer.entity.DevPage;
import cc.allio.turbo.modules.developer.service.IDevBoService;
import cc.allio.turbo.modules.developer.service.IDevPageService;
import cc.allio.uno.core.util.JsonUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dev/page")
@AllArgsConstructor
@Tag(name = "页面管理")
public class DevPageController extends CategoryServiceTurboCrudController<DevPage, IDevPageService> {

    private final IDevBoService boService;

    @PostMapping("/deploy/{menuId}")
    @Operation(description = "页面发布")
    public R<Boolean> deploy(@RequestBody DevPage page, @PathVariable("menuId") Long menuId) {
        boolean deploy = getService().deploy(page, menuId);
        return R.ok(deploy);
    }

    @GetMapping("/pageView/{pageId}")
    @Operation(description = "页面视图")
    public R<PageView> pageView(@PathVariable("pageId") Long pageId) throws BizException {
        DevPage page = getService().getById(pageId);
        if (page == null) {
            throw new BizException(DevCodes.PAGE_NOT_FOUND);
        }
        // 检查bo对象
        Long boId = page.getBoId();
        Boolean check = boService.check(boId);
        if (Boolean.FALSE.equals(check)) {
            throw new BizException(DevCodes.BO_NOT_FOUND);
        }
        PageView pageView = new PageView();
        pageView.setId(page.getId());
        pageView.setName(page.getName());
        pageView.setCode(page.getCode());
        pageView.setRoute(page.getRoute());
        pageView.setFormId(page.getFormId());
        pageView.setBoId(page.getBoId());
        pageView.setDatasetId(page.getDatasetId());
        DataView dataView = JsonUtils.parse(page.getDataview(), DataView.class);
        pageView.setDataView(dataView);
        return R.ok(pageView);
    }
}
