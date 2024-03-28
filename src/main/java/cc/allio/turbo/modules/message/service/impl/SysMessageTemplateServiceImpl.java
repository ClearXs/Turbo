package cc.allio.turbo.modules.message.service.impl;

import cc.allio.turbo.common.db.constant.StorageType;
import cc.allio.turbo.common.db.uno.repository.DS;
import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl;
import cc.allio.turbo.modules.message.entity.SysMessageTemplate;
import cc.allio.turbo.modules.message.service.ISysMessageTemplateService;
import org.springframework.stereotype.Service;

@Service
@DS(StorageType.MONGODB)
public class SysMessageTemplateServiceImpl extends SimpleTurboCrudRepositoryServiceImpl<SysMessageTemplate> implements ISysMessageTemplateService {
}
