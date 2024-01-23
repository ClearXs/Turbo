package cc.allio.turbo.modules.developer.controller;

import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.common.web.CategoryServiceTurboCrudController;
import cc.allio.turbo.common.web.R;
import cc.allio.turbo.modules.developer.domain.BoSchema;
import cc.allio.turbo.modules.developer.entity.DevBo;
import cc.allio.turbo.modules.developer.service.IDevBoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dev/bo")
@AllArgsConstructor
@Tag(name = "业务对象管理")
public class DevBoController extends CategoryServiceTurboCrudController<DevBo, IDevBoService> {

    /**
     * 获取bo schema对象
     */
    @GetMapping("/schema/{boId}")
    @Operation(summary = "获取bo schema对象")
    public R<BoSchema> schema(@PathVariable("boId") Long boId) throws BizException {
        BoSchema schema = getService().toSchema(boId);
        return ok(schema);
    }

}
