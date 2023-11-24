package cc.allio.uno.turbo.modules.system.service.impl;

import cc.allio.uno.turbo.common.mybatis.service.impl.TurboCrudServiceImpl;
import cc.allio.uno.turbo.modules.system.entity.SysUserRole;
import cc.allio.uno.turbo.modules.system.mapper.SysUserRoleMapper;
import cc.allio.uno.turbo.modules.system.service.ISysUserRoleService;
import org.springframework.stereotype.Service;

@Service
public class SysUserRoleServiceImpl extends TurboCrudServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {
}
