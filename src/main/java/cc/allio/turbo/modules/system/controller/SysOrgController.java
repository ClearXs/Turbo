package cc.allio.turbo.modules.system.controller;

import cc.allio.turbo.common.web.TurboTreeCrudController;
import cc.allio.turbo.modules.system.entity.SysOrg;
import cc.allio.turbo.modules.system.domain.SysOrgTree;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/org")
@AllArgsConstructor
@Tag(name = "组织")
public class SysOrgController extends TurboTreeCrudController<SysOrg, SysOrgTree> {
}
