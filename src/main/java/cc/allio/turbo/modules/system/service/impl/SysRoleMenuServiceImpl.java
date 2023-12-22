package cc.allio.turbo.modules.system.service.impl;

import cc.allio.turbo.common.mybatis.service.impl.TurboCrudServiceImpl;
import cc.allio.turbo.modules.system.entity.SysRoleMenu;
import cc.allio.turbo.modules.system.mapper.SysRoleMenuMapper;
import cc.allio.turbo.modules.system.service.ISysRoleMenuService;
import org.springframework.stereotype.Service;

@Service
public class SysRoleMenuServiceImpl extends TurboCrudServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {
}
