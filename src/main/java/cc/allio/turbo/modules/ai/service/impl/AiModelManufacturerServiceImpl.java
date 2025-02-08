package cc.allio.turbo.modules.ai.service.impl;

import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl;
import cc.allio.turbo.modules.ai.entity.AiModelManufacturer;
import cc.allio.turbo.modules.ai.service.IAiModelManufacturerService;
import org.springframework.stereotype.Service;

@Service
public class AiModelManufacturerServiceImpl extends SimpleTurboCrudRepositoryServiceImpl<AiModelManufacturer> implements IAiModelManufacturerService {
}
