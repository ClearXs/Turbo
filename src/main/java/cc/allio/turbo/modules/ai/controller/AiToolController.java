package cc.allio.turbo.modules.ai.controller;

import cc.allio.turbo.common.web.GenericTurboCrudController;
import cc.allio.turbo.modules.ai.entity.AiTool;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai/tool")
@AllArgsConstructor
@Tag(name = "tool")
public class AiToolController extends GenericTurboCrudController<AiTool> {
}
