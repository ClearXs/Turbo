package cc.allio.uno.turbo.modules.system.controller;

import cc.allio.uno.turbo.common.web.TurboTreeCrudController;
import cc.allio.uno.turbo.modules.system.entity.SysDic;
import cc.allio.uno.turbo.modules.system.vo.SysDicTree;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/dic")
@AllArgsConstructor
@Tag(name = "字典")
public class SysDicController extends TurboTreeCrudController<SysDicTree, SysDic> {

}
