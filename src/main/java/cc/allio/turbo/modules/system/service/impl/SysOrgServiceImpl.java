package cc.allio.turbo.modules.system.service.impl;

import cc.allio.turbo.common.mybatis.service.impl.TurboTreeCrudServiceImpl;
import cc.allio.turbo.modules.system.entity.SysOrg;
import cc.allio.turbo.modules.system.mapper.SysOrgMapper;
import cc.allio.turbo.modules.system.service.ISysOrgService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SysOrgServiceImpl extends TurboTreeCrudServiceImpl<SysOrgMapper, SysOrg> implements ISysOrgService {

}
