package cc.allio.turbo.modules.office.controller;

import cc.allio.turbo.common.web.TurboTreeCrudController;
import cc.allio.turbo.modules.office.domain.FolderTree;
import cc.allio.turbo.modules.office.entity.Folder;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/office/folder")
@Tag(name = "文件夹")
public class FolderController extends TurboTreeCrudController<Folder, FolderTree> {

}
