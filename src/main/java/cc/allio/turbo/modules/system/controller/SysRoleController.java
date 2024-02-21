package cc.allio.turbo.modules.system.controller;

import cc.allio.turbo.common.web.R;
import cc.allio.turbo.common.web.TurboCrudController;
import cc.allio.turbo.modules.system.dto.GrantPermissionDTO;
import cc.allio.turbo.modules.system.entity.SysRole;
import cc.allio.turbo.modules.system.service.ISysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sys/role")
@AllArgsConstructor
@Tag(name = "角色")
public class SysRoleController extends TurboCrudController<SysRole, SysRole, ISysRoleService> {

    @PostMapping("/grant")
    @Operation(summary = "授权")
    public R<Boolean> grant(@RequestBody GrantPermissionDTO grantPermission) {
        boolean grant = getService().grant(grantPermission);
        return ok(grant);
    }

}
