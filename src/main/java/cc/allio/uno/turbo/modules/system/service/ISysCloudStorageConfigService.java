package cc.allio.uno.turbo.modules.system.service;

import cc.allio.uno.turbo.common.mybatis.service.ITurboCrudService;
import cc.allio.uno.turbo.modules.system.entity.SysCloudStorageConfig;

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
