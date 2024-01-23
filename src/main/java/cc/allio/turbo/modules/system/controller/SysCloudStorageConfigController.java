package cc.allio.turbo.modules.system.controller;

import cc.allio.turbo.common.web.R;
import cc.allio.turbo.common.web.TurboCrudController;
import cc.allio.turbo.modules.system.entity.SysCloudStorageConfig;
import cc.allio.turbo.modules.system.service.ISysCloudStorageConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sys/cloud-storage-config")
@AllArgsConstructor
@Tag(name = "云存储配置")
public class SysCloudStorageConfigController
        extends TurboCrudController<SysCloudStorageConfig, SysCloudStorageConfig, ISysCloudStorageConfigService> {

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

