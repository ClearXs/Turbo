package cc.allio.turbo.modules.development.service.impl;

import cc.allio.turbo.common.db.mybatis.service.impl.TurboCrudServiceImpl;
import cc.allio.turbo.modules.development.entity.DevForm;
import cc.allio.turbo.modules.development.mapper.DevFormMapper;
import cc.allio.turbo.modules.development.service.IDevFormService;
import org.springframework.stereotype.Service;

@Service
public class DevFormServiceImpl extends TurboCrudServiceImpl<DevFormMapper, DevForm> implements IDevFormService {
    @Override
    public boolean publish(DevForm form) {
        return false;
    }
}
