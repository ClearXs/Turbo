package cc.allio.turbo.modules.office.controller;

import cc.allio.turbo.common.web.TurboCrudController;
import cc.allio.turbo.modules.office.entity.DocTemplate;
import cc.allio.turbo.modules.office.service.IDocTemplateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/office/doc/template")
@Tag(name = "文档模板")
public class DocTemplateController extends TurboCrudController<DocTemplate, DocTemplate, IDocTemplateService> {
}
