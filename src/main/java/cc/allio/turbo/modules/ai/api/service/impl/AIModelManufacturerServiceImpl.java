package cc.allio.turbo.modules.ai.api.service.impl;

import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl;
import cc.allio.turbo.modules.ai.api.entity.AIModelManufacturer;
import cc.allio.turbo.modules.ai.api.service.IAIModelManufacturerService;
import org.springframework.stereotype.Service;

@Service
public class AIModelManufacturerServiceImpl extends SimpleTurboCrudRepositoryServiceImpl<AIModelManufacturer> implements IAIModelManufacturerService {
}
