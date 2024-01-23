package cc.allio.turbo.modules.system.service.impl;

import cc.allio.turbo.modules.system.constant.CsType;
import cc.allio.turbo.modules.system.service.ISysCloudStorageConfigService;
import cc.allio.turbo.common.db.mybatis.service.impl.TurboCrudServiceImpl;
import cc.allio.turbo.extension.oss.OssExecutorFactory;
import cc.allio.turbo.extension.oss.OssTrait;
import cc.allio.turbo.extension.oss.OssUpdateEvent;
import cc.allio.turbo.common.constant.Enable;
import cc.allio.turbo.modules.system.entity.SysCloudStorageConfig;
import cc.allio.turbo.modules.system.mapper.SysCloudStorageConfigMapper;
import cc.allio.uno.core.util.BeanUtils;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import reactor.util.function.Tuples;

@Service
public class SysCloudStorageConfigServiceImpl extends TurboCrudServiceImpl<SysCloudStorageConfigMapper, SysCloudStorageConfig>
        implements ISysCloudStorageConfigService, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @PostConstruct
    public void afterSetup() {
        OssExecutorFactory.setLazyLoader(() -> {
            SysCloudStorageConfig enableConfig = getEnableConfig();
            if (enableConfig != null) {
                OssTrait ossTrait = BeanUtils.copy(enableConfig, OssTrait.class);
                return Tuples.of(enableConfig.getProvider().getValue(), ossTrait);
            } else {
                return null;
            }
        });
    }

    @Override
    public boolean saveOrUpdate(SysCloudStorageConfig entity) {
        boolean saveOrUpdate = super.saveOrUpdate(entity);
        if (Enable.ENABLE == entity.getEnable()) {
            fireEnable();
        }
        return saveOrUpdate;
    }

    @Override
    public boolean saveOrUpdate(SysCloudStorageConfig entity, Wrapper<SysCloudStorageConfig> updateWrapper) {
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
                    Wrappers.<SysCloudStorageConfig>lambdaUpdate()
                            .set(SysCloudStorageConfig::getEnable, Enable.DISABLE)
                            .ne(SysCloudStorageConfig::getId, id);
            var updateSpecific =
                    Wrappers.<SysCloudStorageConfig>lambdaUpdate()
                            .set(SysCloudStorageConfig::getEnable, Enable.ENABLE)
                            .eq(SysCloudStorageConfig::getId, id);
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
                Wrappers.<SysCloudStorageConfig>lambdaUpdate()
                        .set(SysCloudStorageConfig::getEnable, Enable.DISABLE)
                        .eq(SysCloudStorageConfig::getId, id);
        return update(updateDisable);
    }

    /**
     * 在事件总线中发布当前系统中生效的云存储
     */
    private void fireEnable() {
        SysCloudStorageConfig enableConfig = getEnableConfig();
        if (enableConfig != null && CsType.OSS == enableConfig.getCsType()) {
            OssTrait ossTrait = BeanUtils.copy(enableConfig, OssTrait.class);
            applicationContext.publishEvent(new OssUpdateEvent(this, enableConfig.getProvider().getValue(), ossTrait));
        }
    }

    /**
     * 获取生效的配置
     *
     * @return maybe null
     */
    private SysCloudStorageConfig getEnableConfig() {
        try {
            return getOne(Wrappers.<SysCloudStorageConfig>lambdaQuery().eq(SysCloudStorageConfig::getEnable, Enable.ENABLE));
        } catch (Throwable ex) {
            return null;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
