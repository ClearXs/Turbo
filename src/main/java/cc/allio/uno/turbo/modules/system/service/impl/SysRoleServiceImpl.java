package cc.allio.uno.turbo.modules.system.service.impl;

import cc.allio.uno.turbo.common.mybatis.service.impl.TurboCrudServiceImpl;
import cc.allio.uno.turbo.modules.system.dto.GrantPermissionDTO;
import cc.allio.uno.turbo.modules.system.entity.SysRole;
import cc.allio.uno.turbo.modules.system.entity.SysRoleMenu;
import cc.allio.uno.turbo.modules.system.service.ISysRoleMenuService;
import cc.allio.uno.turbo.modules.system.mapper.SysRoleMapper;
import cc.allio.uno.turbo.modules.system.service.ISysRoleService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class SysRoleServiceImpl extends TurboCrudServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    private final ISysRoleMenuService sysRoleMenuService;

    @Override
    public List<SysRole> getRoleByCodes(List<String> codes) {
        return list(lambdaQuery().in(SysRole::getCode, codes));
    }

    @Override
    public List<Long> getRoleMenuIdByIds(List<Long> roleIds) {
        return getBaseMapper().findRoleMenuIdByIds(roleIds);
    }

    @Override
    public List<Long> getRoleMenuIdByCodes(List<String> codes) {
        return getBaseMapper().findRoleMenuIdByCodes(codes);
    }

    @Override
    @Transactional
    public boolean grant(GrantPermissionDTO grantPermission) {
        List<SysRoleMenu> sysRoleMenus = grantPermission.getMenuId()
                .stream()
                .map(menuId -> {
                    SysRoleMenu sysRoleMenu = new SysRoleMenu();
                    sysRoleMenu.setRoleId(grantPermission.getRoleId());
                    sysRoleMenu.setMenuId(menuId);
                    return sysRoleMenu;
                })
                .toList();
        // 移除当前角色绑定菜单
        sysRoleMenuService.remove(Wrappers.<SysRoleMenu>lambdaQuery().eq(SysRoleMenu::getRoleId, grantPermission.getRoleId()));
        // TODO 数据权限
        // TODO 接口权限
        return sysRoleMenuService.saveBatch(sysRoleMenus);
    }

    @Override
    public List<SysRole> findRolesByUserId(Long userId) {
        return getBaseMapper().findSysRoleByUserId(userId);
    }
}
