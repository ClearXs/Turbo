package cc.allio.turbo.modules.system.service.impl;

import cc.allio.turbo.common.db.mybatis.service.impl.TurboCrudServiceImpl;
import cc.allio.turbo.modules.system.entity.SysUserRole;
import cc.allio.turbo.modules.system.mapper.SysUserRoleMapper;
import cc.allio.turbo.modules.system.service.ISysUserRoleService;
import org.springframework.stereotype.Service;

@Service
public class SysUserRoleServiceImpl extends TurboCrudServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {
}
