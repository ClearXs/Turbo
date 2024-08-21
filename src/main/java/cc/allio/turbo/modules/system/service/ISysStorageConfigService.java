package cc.allio.turbo.modules.system.service;

import cc.allio.turbo.common.db.mybatis.service.ITurboCrudService;
import cc.allio.turbo.modules.system.entity.SysStorageConfig;

public interface ISysStorageConfigService extends ITurboCrudService<SysStorageConfig> {

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
