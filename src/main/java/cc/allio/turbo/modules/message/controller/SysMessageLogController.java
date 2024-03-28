package cc.allio.turbo.modules.message.controller;

import cc.allio.turbo.common.web.TurboCrudController;
import cc.allio.turbo.modules.message.entity.SysMessageLog;
import cc.allio.turbo.modules.message.service.ISysMessageLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/message/log")
@Tag(name = "消息日志")
public class SysMessageLogController extends TurboCrudController<SysMessageLog, SysMessageLog, ISysMessageLogService> {
}
