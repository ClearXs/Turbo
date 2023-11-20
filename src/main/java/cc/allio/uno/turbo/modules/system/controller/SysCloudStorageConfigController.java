package cc.allio.uno.turbo.modules.system.controller;

import cc.allio.uno.turbo.common.web.R;
import cc.allio.uno.turbo.common.web.TurboCrudController;
import cc.allio.uno.turbo.common.mybatis.entity.BaseEntity;
import cc.allio.uno.turbo.common.constant.Enable;
import cc.allio.uno.turbo.modules.system.entity.SysCloudStorageConfig;
import cc.allio.uno.turbo.modules.system.service.ISysCloudStorageConfigService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sys/cloud-storage-config")
@AllArgsConstructor
@Tag(name = "云存储配置")
public class SysCloudStorageConfigController extends TurboCrudController<SysCloudStorageConfig> {

    private final ISysCloudStorageConfigService cloudStorageConfigService;

    @Override
    public R save(@Validated @RequestBody SysCloudStorageConfig sysCloudStorageConfig) {
        boolean save = cloudStorageConfigService.save(sysCloudStorageConfig);
        return ok(save);
    }

    @Override
    public R edit(@Validated @RequestBody SysCloudStorageConfig sysCloudStorageConfig) {
        boolean edit = cloudStorageConfigService.update(sysCloudStorageConfig, Wrappers.<SysCloudStorageConfig>lambdaQuery().eq(SysCloudStorageConfig::getId, sysCloudStorageConfig.getId()));
        return ok(edit);
    }

    @Override
    public R saveOrUpdate(@Validated @RequestBody SysCloudStorageConfig sysCloudStorageConfig) {
        boolean edit = cloudStorageConfigService.saveOrUpdate(sysCloudStorageConfig, Wrappers.<SysCloudStorageConfig>lambdaQuery().eq(SysCloudStorageConfig::getId, sysCloudStorageConfig.getId()));
        return ok(edit);
    }

    @Override
    public R batchSave(List<SysCloudStorageConfig> entity) {
        boolean batch = cloudStorageConfigService.saveBatch(entity);
        return ok(batch);
    }

    @Override
    public R delete(@RequestBody List<Long> ids) {
        boolean removed = cloudStorageConfigService.removeByIds(ids);
        return ok(removed);
    }

    @Override
    public R<SysCloudStorageConfig> details(long id) {
        SysCloudStorageConfig sysCloudStorageConfig = cloudStorageConfigService.getById(id);
        return ok(sysCloudStorageConfig);
    }

    @Override
    public R<List<SysCloudStorageConfig>> list(SysCloudStorageConfig sysCloudStorageConfig) {
        List<SysCloudStorageConfig> list = cloudStorageConfigService.list(Wrappers.lambdaQuery(sysCloudStorageConfig).orderByDesc(BaseEntity::getUpdatedTime));
        return ok(list);
    }

    @Override
    public R<IPage<SysCloudStorageConfig>> page(Page page, SysCloudStorageConfig sysCloudStorageConfig) {
        IPage<SysCloudStorageConfig> sysCloudStorageConfigPage = cloudStorageConfigService.page(page, Wrappers.lambdaQuery(sysCloudStorageConfig).orderByDesc(BaseEntity::getUpdatedTime));
        return ok(sysCloudStorageConfigPage);
    }

    @PutMapping("/enable")
    @Operation(summary = "是否启用")
    public R enable(long id, Enable enable) {
        return bool(cloudStorageConfigService.enable(id, enable));
    }
}

