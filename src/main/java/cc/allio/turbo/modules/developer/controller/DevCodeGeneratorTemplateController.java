package cc.allio.turbo.modules.developer.controller;

import cc.allio.turbo.common.web.CategoryServiceTurboCrudController;
import cc.allio.turbo.modules.developer.entity.DevCodeGenerateTemplate;
import cc.allio.turbo.modules.developer.service.IDevCodeGeneratorTemplateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dev/code/generate/template")
@AllArgsConstructor
@Tag(name = "代码生成模板")
public class DevCodeGeneratorTemplateController extends CategoryServiceTurboCrudController<DevCodeGenerateTemplate, IDevCodeGeneratorTemplateService> {
}
