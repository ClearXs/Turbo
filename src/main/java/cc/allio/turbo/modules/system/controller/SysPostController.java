package cc.allio.turbo.modules.system.controller;

import cc.allio.turbo.common.web.GenericTurboCrudController;
import cc.allio.turbo.modules.system.entity.SysPost;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/post")
@AllArgsConstructor
@Tag(name = "岗位")
public class SysPostController extends GenericTurboCrudController<SysPost> {
}
