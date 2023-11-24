package cc.allio.uno.turbo.modules.system.controller;

import cc.allio.uno.turbo.common.web.TurboTreeCrudController;
import cc.allio.uno.turbo.modules.system.entity.SysMenu;
import cc.allio.uno.turbo.modules.system.vo.SysMenuTree;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sys/menu")
@AllArgsConstructor
@Tag(name = "菜单")
public class SysMenuController extends TurboTreeCrudController<SysMenuTree, SysMenu> {

    @Override
    protected Class<SysMenuTree> getTreeType() {
        return SysMenuTree.class;
    }
}
