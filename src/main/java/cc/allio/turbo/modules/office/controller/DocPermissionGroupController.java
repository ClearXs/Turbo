package cc.allio.turbo.modules.office.controller;

import cc.allio.turbo.common.web.TurboCrudController;
import cc.allio.turbo.modules.office.entity.DocPermissionGroup;
import cc.allio.turbo.modules.office.service.IDocPermissionGroupService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/office/doc/permissionGroup")
@Tag(name = "文档权限组")
public class DocPermissionGroupController extends TurboCrudController<DocPermissionGroup, DocPermissionGroup, IDocPermissionGroupService> {
}
