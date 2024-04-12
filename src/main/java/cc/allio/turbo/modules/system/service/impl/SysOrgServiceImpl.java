package cc.allio.turbo.modules.system.service.impl;

import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudTreeRepositoryServiceImpl;
import cc.allio.turbo.modules.system.entity.SysOrg;
import cc.allio.turbo.modules.system.service.ISysOrgService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SysOrgServiceImpl extends SimpleTurboCrudTreeRepositoryServiceImpl<SysOrg> implements ISysOrgService {

}
