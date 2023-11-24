package cc.allio.uno.turbo.modules.system.service.impl;

import cc.allio.uno.turbo.common.mybatis.service.impl.TurboTreeCrudServiceImpl;
import cc.allio.uno.turbo.modules.system.entity.SysOrg;
import cc.allio.uno.turbo.modules.system.mapper.SysOrgMapper;
import cc.allio.uno.turbo.modules.system.service.ISysOrgService;
import org.springframework.stereotype.Service;

@Service
public class SysOrgServiceImpl extends TurboTreeCrudServiceImpl<SysOrgMapper, SysOrg> implements ISysOrgService {
}
