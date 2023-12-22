package cc.allio.turbo.modules.system.controller;

import cc.allio.turbo.common.web.TurboTreeCrudController;
import cc.allio.turbo.modules.system.entity.SysMenu;
import cc.allio.turbo.modules.system.vo.SysMenuTree;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sys/menu")
@AllArgsConstructor
@Tag(name = "菜单")
public class SysMenuController extends TurboTreeCrudController<SysMenuTree, SysMenu> {

}
