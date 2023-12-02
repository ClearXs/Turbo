package cc.allio.uno.turbo.modules.system.controller;

import cc.allio.uno.turbo.common.web.GenericTurboCrudController;
import cc.allio.uno.turbo.modules.system.entity.SysUserPost;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/user-post")
@AllArgsConstructor
@Tag(name = "用户岗位")
public class SysUserPostController extends GenericTurboCrudController<SysUserPost> {
}
