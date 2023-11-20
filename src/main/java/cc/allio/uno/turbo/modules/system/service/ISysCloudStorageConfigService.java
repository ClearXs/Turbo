package cc.allio.uno.turbo.modules.system.service;

import cc.allio.uno.turbo.common.constant.Enable;
import cc.allio.uno.turbo.modules.system.entity.SysCloudStorageConfig;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ISysCloudStorageConfigService extends IService<SysCloudStorageConfig> {

    /**
     * 更新
     * @param id
     * @param enable
     * @return
     */
    boolean enable(long id, Enable enable);
}
