package cc.allio.turbo.modules.system.service.impl;

import cc.allio.turbo.common.db.mybatis.service.impl.TurboTreeCrudServiceImpl;
import cc.allio.turbo.modules.system.entity.SysMenu;
import cc.allio.turbo.modules.system.mapper.SysMenuMapper;
import cc.allio.turbo.modules.system.service.ISysMenuService;
import org.springframework.stereotype.Service;

@Service
public class SysMenuServiceImpl extends TurboTreeCrudServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {
}
