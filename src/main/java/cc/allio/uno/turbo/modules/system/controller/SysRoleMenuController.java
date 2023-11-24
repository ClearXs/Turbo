package cc.allio.uno.turbo.modules.system.controller;

import cc.allio.uno.turbo.common.web.GenericTurboCrudController;
import cc.allio.uno.turbo.modules.system.entity.SysRoleMenu;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/role-menu")
@AllArgsConstructor
@Tag(name = "角色菜单")
public class SysRoleMenuController extends GenericTurboCrudController<SysRoleMenu> {

}
