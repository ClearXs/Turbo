package cc.allio.turbo.modules.message.controller;

import cc.allio.turbo.common.web.TurboCrudController;
import cc.allio.turbo.modules.message.entity.SysMessageConfig;
import cc.allio.turbo.modules.message.service.ISysMessageConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/message/config")
@Tag(name = "消息配置")
public class SysMessageConfigController extends TurboCrudController<SysMessageConfig, SysMessageConfig, ISysMessageConfigService> {
}
