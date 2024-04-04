package cc.allio.turbo.modules.system.service.impl;

import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl;
import cc.allio.turbo.modules.system.entity.SysThirdUser;
import cc.allio.turbo.modules.system.service.ISysThirdUserService;
import org.springframework.stereotype.Service;

@Service
public class SysThirdUserServiceImpl extends SimpleTurboCrudRepositoryServiceImpl<SysThirdUser> implements ISysThirdUserService {
}
