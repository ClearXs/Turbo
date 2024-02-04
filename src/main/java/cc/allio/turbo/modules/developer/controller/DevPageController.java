package cc.allio.turbo.modules.developer.controller;

import cc.allio.turbo.common.web.CategoryServiceTurboCrudController;
import cc.allio.turbo.common.web.R;
import cc.allio.turbo.modules.developer.domain.view.DataView;
import cc.allio.turbo.modules.developer.entity.DevPage;
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

    @PostMapping("/deploy/{menuId}")
    @Operation(description = "页面发布")
    public R<Boolean> deploy(@RequestBody DevPage page, @PathVariable("menuId") Long menuId) {
        boolean deploy = getService().deploy(page, menuId);
        return R.ok(deploy);
    }

    @GetMapping("/dataView/{pageId}")
    @Operation(description = "数据视图")
    public R<DataView> dataView(@PathVariable("pageId") Long pageId) {
        DevPage page = getService().getById(pageId);
        if (page != null) {
            DataView dataView = JsonUtils.parse(page.getDataview(), DataView.class);
            return R.ok(dataView);
        }
        return R.ok();
    }
}
