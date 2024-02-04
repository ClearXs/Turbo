package cc.allio.turbo.modules.developer.controller;

import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.common.web.CategoryServiceTurboCrudController;
import cc.allio.turbo.common.web.R;
import cc.allio.turbo.modules.developer.domain.BoSchema;
import cc.allio.turbo.modules.developer.entity.DevBo;
import cc.allio.turbo.modules.developer.service.IDevBoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dev/bo")
@AllArgsConstructor
@Tag(name = "业务对象管理")
public class DevBoController extends CategoryServiceTurboCrudController<DevBo, IDevBoService> {

    @GetMapping("/schema/{boId}")
    @Operation(summary = "获取bo schema对象")
    public R<BoSchema> schema(@PathVariable("boId") Long boId) throws BizException {
        BoSchema schema = getService().cacheToSchema(boId);
        return ok(schema);
    }

    @GetMapping("/materialize/{boId}")
    @Operation(summary = "物化bo对象")
    public R<Boolean> materialize(@PathVariable("boId") Long boId) {
        Boolean materialized = getService().materialize(boId);
        return R.ok(materialized);
    }

    @PostMapping("/save-schema")
    @Operation(summary = "保存 bo schema")
    public R<Boolean> saveSchema(@Validated @NotNull @RequestBody BoSchema boSchema) {
        boolean schema = getService().saveBoSchema(boSchema);
        return ok(schema);
    }
}
