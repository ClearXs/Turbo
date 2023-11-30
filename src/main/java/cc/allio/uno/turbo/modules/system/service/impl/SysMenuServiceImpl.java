package cc.allio.uno.turbo.modules.system.service.impl;

import cc.allio.uno.turbo.common.mybatis.service.impl.TurboTreeCrudServiceImpl;
import cc.allio.uno.turbo.modules.system.entity.SysMenu;
import cc.allio.uno.turbo.modules.system.mapper.SysMenuMapper;
import cc.allio.uno.turbo.modules.system.service.ISysMenuService;
import org.springframework.stereotype.Service;

@Service
public class SysMenuServiceImpl extends TurboTreeCrudServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {
}
