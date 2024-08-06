package cc.allio.turbo.modules.office.controller;

import cc.allio.turbo.common.web.TurboCrudController;
import cc.allio.turbo.modules.office.entity.DocTrash;
import cc.allio.turbo.modules.office.service.IDocTrashService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/office/doc/trash")
@Tag(name = "文档回收站")
public class DocTrashController extends TurboCrudController<DocTrash, DocTrash, IDocTrashService> {
}
