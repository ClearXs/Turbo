package cc.allio.uno.turbo.modules.system.service;

import cc.allio.uno.turbo.common.constant.Enable;
import cc.allio.uno.turbo.common.mybatis.service.ITurboCrudService;
import cc.allio.uno.turbo.modules.system.entity.SysCloudStorageConfig;

public interface ISysCloudStorageConfigService extends ITurboCrudService<SysCloudStorageConfig> {

    /**
     * 更新
     * @param id
     * @param enable
     * @return
     */
    boolean enable(long id, Enable enable);
}
