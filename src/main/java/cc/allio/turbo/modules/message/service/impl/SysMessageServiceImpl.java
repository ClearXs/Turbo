package cc.allio.turbo.modules.message.service.impl;

import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl;
import cc.allio.turbo.modules.message.entity.SysMessage;
import cc.allio.turbo.modules.message.service.ISysMessageService;
import org.springframework.stereotype.Service;

@Service
public class SysMessageServiceImpl extends SimpleTurboCrudRepositoryServiceImpl<SysMessage> implements ISysMessageService {
}
