package cc.allio.turbo.modules.developer.service.impl;

import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl;
import cc.allio.turbo.modules.developer.entity.DevCodeGenerateTemplate;
import cc.allio.turbo.modules.developer.service.IDevCodeGeneratorTemplateService;
import org.springframework.stereotype.Service;

@Service
public class DevCodeGeneratorTemplateServiceImpl extends SimpleTurboCrudRepositoryServiceImpl<DevCodeGenerateTemplate> implements IDevCodeGeneratorTemplateService {
}
