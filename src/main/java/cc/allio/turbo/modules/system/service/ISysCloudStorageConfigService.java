package cc.allio.turbo.modules.system.service;

import cc.allio.turbo.common.mybatis.service.ITurboCrudService;
import cc.allio.turbo.modules.system.entity.SysCloudStorageConfig;

public interface ISysCloudStorageConfigService extends ITurboCrudService<SysCloudStorageConfig> {

    /**
     * 启用
     *
     * @param id
     * @return
     */
    boolean enable(long id);

    /**
     * 禁用
     *
     * @param id
     * @return
     */
    boolean disable(long id);
}
