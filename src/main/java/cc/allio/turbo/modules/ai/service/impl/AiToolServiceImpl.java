package cc.allio.turbo.modules.ai.service.impl;

import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl;
import cc.allio.turbo.modules.ai.entity.AiTool;
import cc.allio.turbo.modules.ai.service.IAiToolService;
import org.springframework.stereotype.Service;

@Service
public class AiToolServiceImpl extends SimpleTurboCrudRepositoryServiceImpl<AiTool> implements IAiToolService {
}
