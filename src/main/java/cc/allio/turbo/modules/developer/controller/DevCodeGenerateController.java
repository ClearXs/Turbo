package cc.allio.turbo.modules.developer.controller;

import cc.allio.turbo.common.web.CategoryServiceTurboCrudController;
import cc.allio.turbo.modules.developer.entity.DevCodeGenerate;
import cc.allio.turbo.modules.developer.service.IDevCodeGenerateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dev/code/generate")
@AllArgsConstructor
@Tag(name = "代码生成")
public class DevCodeGenerateController extends CategoryServiceTurboCrudController<DevCodeGenerate, IDevCodeGenerateService> {
}
