package cc.allio.turbo.modules.message.service.impl;

import cc.allio.turbo.common.db.constant.StorageType;
import cc.allio.turbo.common.db.uno.repository.DS;
import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl;
import cc.allio.turbo.modules.message.entity.SysMessageConfig;
import cc.allio.turbo.modules.message.service.ISysMessageConfigService;
import org.springframework.stereotype.Service;

@Service
@DS(StorageType.MONGODB)
public class SysMessageConfigServiceImpl extends SimpleTurboCrudRepositoryServiceImpl<SysMessageConfig> implements ISysMessageConfigService {


    @Override
    public String getCacheName() {
        return "sysmessge_config";
    }
}
