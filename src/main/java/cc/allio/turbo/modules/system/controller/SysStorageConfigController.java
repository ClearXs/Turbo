package cc.allio.turbo.modules.system.controller;

import cc.allio.turbo.common.web.R;
import cc.allio.turbo.common.web.TurboCrudController;
import cc.allio.turbo.modules.system.entity.SysStorageConfig;
import cc.allio.turbo.modules.system.service.ISysStorageConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sys/storage-config")
@AllArgsConstructor
@Tag(name = "存储配置")
public class SysStorageConfigController
        extends TurboCrudController<SysStorageConfig, SysStorageConfig, ISysStorageConfigService> {

    @PutMapping("/enable/{id}")
    @Operation(summary = "启用")
    public R enable(@PathVariable("id") Long id) {
        return bool(getService().enable(id));
    }


    @PutMapping("/disable/{id}")
    @Operation(summary = "禁用")
    public R disable(@PathVariable("id") Long id) {
        return bool(getService().disable(id));
    }
}

