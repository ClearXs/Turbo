package cc.allio.turbo.modules.system.controller;

import cc.allio.turbo.common.web.GenericTurboCrudController;
import cc.allio.turbo.modules.system.entity.SysUserRole;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/user-role")
@AllArgsConstructor
@Tag(name = "用户角色")
public class SysUserRoleController extends GenericTurboCrudController<SysUserRole> {
}
