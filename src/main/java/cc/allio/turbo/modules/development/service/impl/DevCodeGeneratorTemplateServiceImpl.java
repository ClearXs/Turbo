package cc.allio.turbo.modules.development.service.impl;

import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl;
import cc.allio.turbo.modules.development.entity.DevCodeGenerateTemplate;
import cc.allio.turbo.modules.development.service.IDevCodeGeneratorTemplateService;
import org.springframework.stereotype.Service;

@Service
public class DevCodeGeneratorTemplateServiceImpl extends SimpleTurboCrudRepositoryServiceImpl<DevCodeGenerateTemplate> implements IDevCodeGeneratorTemplateService {
}
