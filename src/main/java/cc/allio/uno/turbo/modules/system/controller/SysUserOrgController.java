package cc.allio.uno.turbo.modules.system.controller;

import cc.allio.uno.turbo.common.web.GenericTurboCrudController;
import cc.allio.uno.turbo.modules.system.entity.SysUserOrg;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/user-org")
@AllArgsConstructor
@Tag(name = "用户组织")
public class SysUserOrgController extends GenericTurboCrudController<SysUserOrg> {
}
