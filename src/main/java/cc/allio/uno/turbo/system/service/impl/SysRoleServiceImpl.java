package cc.allio.uno.turbo.system.service.impl;

import cc.allio.uno.turbo.system.dto.GrantPermissionDTO;
import cc.allio.uno.turbo.system.entity.SysRole;
import cc.allio.uno.turbo.system.entity.SysRoleMenu;
import cc.allio.uno.turbo.system.mapper.SysRoleMapper;
import cc.allio.uno.turbo.system.service.ISysRoleMenuService;
import cc.allio.uno.turbo.system.service.ISysRoleService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

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
    public boolean grant(GrantPermissionDTO grantPermission) {
        // 移除当前角色绑定菜单
        sysRoleMenuService.remove(Wrappers.<SysRoleMenu>lambdaQuery().eq(SysRoleMenu::getRoleId, grantPermission.getRoleId()));
        List<SysRoleMenu> sysRoleMenus = grantPermission.getMenuId()
                .stream()
                .map(menuId -> {
                    SysRoleMenu sysRoleMenu = new SysRoleMenu();
                    sysRoleMenu.setRoleId(grantPermission.getRoleId());
                    sysRoleMenu.setMenuId(menuId);
                    return sysRoleMenu;
                })
                .toList();
        // TODO 数据权限
        // TODO 接口权限
        return sysRoleMenuService.saveBatch(sysRoleMenus);
    }

    @Override
    public List<SysRole> getRolesByUser(Long userId) {
        return getBaseMapper().findSysRoleByUserId(userId);
    }
}
