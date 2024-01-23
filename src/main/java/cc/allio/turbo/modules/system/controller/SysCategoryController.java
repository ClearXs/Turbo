package cc.allio.turbo.modules.system.controller;

import cc.allio.turbo.common.web.TurboTreeCrudController;
import cc.allio.turbo.modules.system.entity.SysCategory;
import cc.allio.turbo.modules.system.domain.SysCategoryTree;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/category")
@AllArgsConstructor
@Tag(name = "分类")
public class SysCategoryController extends TurboTreeCrudController<SysCategory, SysCategoryTree> {
}
