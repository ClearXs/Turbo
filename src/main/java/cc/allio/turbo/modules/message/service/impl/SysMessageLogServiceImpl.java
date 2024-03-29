package cc.allio.turbo.modules.message.service.impl;

import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl;
import cc.allio.turbo.modules.message.entity.SysMessageLog;
import cc.allio.turbo.modules.message.service.ISysMessageLogService;
import org.springframework.stereotype.Service;

@Service
public class SysMessageLogServiceImpl extends SimpleTurboCrudRepositoryServiceImpl<SysMessageLog> implements ISysMessageLogService {
}
