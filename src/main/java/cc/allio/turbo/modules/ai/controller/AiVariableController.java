package cc.allio.turbo.modules.ai.controller;

import cc.allio.turbo.common.web.GenericTurboCrudController;
import cc.allio.turbo.modules.ai.entity.AiVariable;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai/variable")
@AllArgsConstructor
@Tag(name = "variable")
public class AiVariableController extends GenericTurboCrudController<AiVariable> {
}
