package cc.allio.turbo.modules.developer.service.impl;

import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl;
import cc.allio.turbo.modules.developer.entity.DevCodeGenerate;
import cc.allio.turbo.modules.developer.service.IDevCodeGenerateService;
import org.springframework.stereotype.Service;

@Service
public class DevCodeGenerateServiceImpl extends SimpleTurboCrudRepositoryServiceImpl<DevCodeGenerate> implements IDevCodeGenerateService {
}
