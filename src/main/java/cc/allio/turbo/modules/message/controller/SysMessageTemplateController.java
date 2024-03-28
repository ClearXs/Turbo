package cc.allio.turbo.modules.message.controller;

import cc.allio.turbo.common.web.TurboCrudController;
import cc.allio.turbo.modules.message.entity.SysMessageTemplate;
import cc.allio.turbo.modules.message.service.ISysMessageTemplateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/message/template")
@Tag(name = "消息模板")
public class SysMessageTemplateController extends TurboCrudController<SysMessageTemplate, SysMessageTemplate, ISysMessageTemplateService> {
}
