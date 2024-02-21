package cc.allio.turbo.modules.developer.service.impl;

import cc.allio.turbo.common.db.mybatis.service.impl.TurboCrudServiceImpl;
import cc.allio.turbo.modules.developer.entity.DevForm;
import cc.allio.turbo.modules.developer.mapper.DevFormMapper;
import cc.allio.turbo.modules.developer.service.IDevFormService;
import org.springframework.stereotype.Service;

@Service
public class DevFormServiceImpl extends TurboCrudServiceImpl<DevFormMapper, DevForm> implements IDevFormService {
    @Override
    public boolean publish(DevForm form) {
        return false;
    }
}
