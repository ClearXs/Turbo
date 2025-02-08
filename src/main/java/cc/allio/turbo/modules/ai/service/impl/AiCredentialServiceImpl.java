package cc.allio.turbo.modules.ai.service.impl;

import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl;
import cc.allio.turbo.modules.ai.entity.AiCredential;
import cc.allio.turbo.modules.ai.service.IAiCredentialService;
import org.springframework.stereotype.Service;

@Service
public class AiCredentialServiceImpl extends SimpleTurboCrudRepositoryServiceImpl<AiCredential> implements IAiCredentialService {
}
