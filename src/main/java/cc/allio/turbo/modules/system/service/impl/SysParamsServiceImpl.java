package cc.allio.turbo.modules.system.service.impl;

import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl;
import cc.allio.turbo.modules.system.entity.SysParams;
import cc.allio.turbo.modules.system.service.ISysParamsService;
import org.springframework.stereotype.Service;

@Service
public class SysParamsServiceImpl extends SimpleTurboCrudRepositoryServiceImpl<SysParams> implements ISysParamsService {
}
