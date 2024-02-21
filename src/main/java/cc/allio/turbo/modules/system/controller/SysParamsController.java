package cc.allio.turbo.modules.system.controller;

import cc.allio.turbo.common.web.GenericTurboCrudController;
import cc.allio.turbo.modules.system.entity.SysParams;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/params")
@AllArgsConstructor
@Tag(name = "参数")
public class SysParamsController extends GenericTurboCrudController<SysParams> {
}
