package cc.allio.turbo.modules.ai.service.impl;

import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl;
import cc.allio.turbo.modules.ai.entity.AICredential;
import cc.allio.turbo.modules.ai.service.IAICredentialService;
import org.springframework.stereotype.Service;

@Service
public class AICredentialServiceImpl extends SimpleTurboCrudRepositoryServiceImpl<AICredential> implements IAICredentialService {
}
