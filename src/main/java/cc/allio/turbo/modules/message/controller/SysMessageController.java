package cc.allio.turbo.modules.message.controller;

import cc.allio.turbo.common.web.TurboCrudController;
import cc.allio.turbo.modules.message.entity.SysMessage;
import cc.allio.turbo.modules.message.service.ISysMessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/message")
@Tag(name = "消息")
public class SysMessageController extends TurboCrudController<SysMessage, SysMessage, ISysMessageService> {
}
