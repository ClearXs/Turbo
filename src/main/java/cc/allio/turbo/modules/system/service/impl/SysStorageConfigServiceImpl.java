package cc.allio.turbo.modules.system.service.impl;

import cc.allio.turbo.extension.oss.*;
import cc.allio.turbo.modules.system.service.ISysStorageConfigService;
import cc.allio.turbo.common.db.mybatis.service.impl.TurboCrudServiceImpl;
import cc.allio.turbo.common.constant.Enable;
import cc.allio.turbo.modules.system.entity.SysStorageConfig;
import cc.allio.turbo.modules.system.mapper.SysStorageConfigMapper;
import cc.allio.turbo.modules.system.wrapper.StorageConfigWrapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class SysStorageConfigServiceImpl extends TurboCrudServiceImpl<SysStorageConfigMapper, SysStorageConfig>
        implements ISysStorageConfigService, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private final OssProperties ossProperties;

    public SysStorageConfigServiceImpl(OssProperties ossProperties) {
        this.ossProperties = ossProperties;
    }

    @PostConstruct
    public void afterSetup() {
        OssExecutorFactory.setLazyLoader(this::getEnabledConfig);
    }

    @Override
    public boolean saveOrUpdate(SysStorageConfig entity) {
        boolean saveOrUpdate = super.saveOrUpdate(entity);
        if (Enable.ENABLE == entity.getEnable()) {
            fireEnable();
        }
        return saveOrUpdate;
    }

    @Override
    public boolean saveOrUpdate(SysStorageConfig entity, Wrapper<SysStorageConfig> updateWrapper) {
        boolean saveOrUpdate = super.saveOrUpdate(entity, updateWrapper);
        if (Enable.ENABLE == entity.getEnable()) {
            fireEnable();
        }
        return saveOrUpdate;
    }

    @Override
    public boolean enable(long id) {
        try {
            var updateAllDisable =
                    Wrappers.<SysStorageConfig>lambdaUpdate()
                            .set(SysStorageConfig::getEnable, Enable.DISABLE)
                            .ne(SysStorageConfig::getId, id);
            var updateSpecific =
                    Wrappers.<SysStorageConfig>lambdaUpdate()
                            .set(SysStorageConfig::getEnable, Enable.ENABLE)
                            .eq(SysStorageConfig::getId, id);
            // 更新所有为disable
            update(updateAllDisable);
            return update(updateSpecific);
        } finally {
            fireEnable();
        }
    }

    @Override
    public boolean disable(long id) {
        var updateDisable =
                Wrappers.<SysStorageConfig>lambdaUpdate()
                        .set(SysStorageConfig::getEnable, Enable.DISABLE)
                        .eq(SysStorageConfig::getId, id);
        return update(updateDisable);
    }

    /**
     * 在事件总线中发布当前系统中生效的云存储
     */
    private void fireEnable() {
        OssTrait trait = getEnabledConfig();
        if (trait != null) {
            applicationContext.publishEvent(new OssUpdateEvent(this, trait));
        }
    }

    /**
     * 获取生效的配置
     *
     * @return maybe null
     */
    private OssTrait getEnabledConfig() {
        Boolean isFetch = ossProperties.getFetch();
        // fetch locally properties
        if (Boolean.TRUE.equals(isFetch)) {
            Provider provider = ossProperties.getProvider();
            String secretKey = ossProperties.getSecretKey();
            String endpoint = ossProperties.getEndpoint();
            String bucket = ossProperties.getBucket();
            String accessKey = ossProperties.getAccessKey();
            OssTrait trait = new OssTrait();
            trait.setProvider(provider);
            trait.setBucket(bucket);
            trait.setAccessKey(accessKey);
            trait.setSecretKey(secretKey);
            trait.setEndpoint(endpoint);
            return trait;
        } else {
            SysStorageConfig storageConfig =
                    getOne(Wrappers.<SysStorageConfig>lambdaQuery().eq(SysStorageConfig::getEnable, Enable.ENABLE));
            if (storageConfig != null) {
                return StorageConfigWrapper.wrapperToOssTrait(storageConfig);
            }
            return null;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
