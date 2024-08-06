package cc.allio.turbo.modules.office.controller;

import cc.allio.turbo.common.web.TurboCrudController;
import cc.allio.turbo.modules.office.entity.DocHistory;
import cc.allio.turbo.modules.office.service.IDocHistoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/office/doc/history")
@Tag(name = "文档版本")
public class DocHistoryController extends TurboCrudController<DocHistory, DocHistory, IDocHistoryService> {
}
