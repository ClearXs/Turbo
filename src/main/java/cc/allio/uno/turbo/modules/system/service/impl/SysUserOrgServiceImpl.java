package cc.allio.uno.turbo.modules.system.service.impl;

import cc.allio.uno.turbo.common.mybatis.service.impl.TurboCrudServiceImpl;
import cc.allio.uno.turbo.modules.system.entity.SysUserOrg;
import cc.allio.uno.turbo.modules.system.mapper.SysUserOrgMapper;
import cc.allio.uno.turbo.modules.system.service.ISysUserOrgService;
import org.springframework.stereotype.Service;

@Service
public class SysUserOrgServiceImpl extends TurboCrudServiceImpl<SysUserOrgMapper, SysUserOrg> implements ISysUserOrgService {
}
