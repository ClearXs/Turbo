package cc.allio.uno.turbo.modules.system.controller;

import cc.allio.uno.turbo.common.web.GenericTurboCrudController;
import cc.allio.uno.turbo.modules.system.entity.SysTenant;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sys/tenant")
@AllArgsConstructor
@Tag(name = "租户")
public class SysTenantController extends GenericTurboCrudController<SysTenant> {

}
