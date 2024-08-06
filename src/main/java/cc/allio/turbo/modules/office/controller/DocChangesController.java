package cc.allio.turbo.modules.office.controller;

import cc.allio.turbo.common.web.TurboCrudController;
import cc.allio.turbo.modules.office.entity.DocChanges;
import cc.allio.turbo.modules.office.service.IDocChangesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/office/doc/changes")
@Tag(name = "文档变更")
public class DocChangesController extends TurboCrudController<DocChanges, DocChanges, IDocChangesService> {

}