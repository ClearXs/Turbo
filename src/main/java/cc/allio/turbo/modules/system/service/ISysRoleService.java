package cc.allio.turbo.modules.system.service;

import cc.allio.turbo.common.mybatis.service.ITurboCrudService;
import cc.allio.turbo.modules.system.dto.GrantPermissionDTO;
import cc.allio.turbo.modules.system.entity.SysRole;

import java.util.List;

public interface ISysRoleService extends ITurboCrudService<SysRole> {

    /**
     * 根据角色编码获取角色实体
     *
     * @param codes codes
     * @return
     */
    List<SysRole> getRoleByCodes(List<String> codes);

    /**
     * 根据角色id获取菜单
     *
     * @param roleIds roleIds
     * @return
     */
    List<Long> getRoleMenuIdByIds(List<Long> roleIds);

    /**
     * 获取角色对应的菜单id
     *
     * @param codes codes
     * @return
     */
    List<Long> getRoleMenuIdByCodes(List<String> codes);

    /**
     * 权限授予
     * <ul>
     *     <li>功能权限</li>
     *     <li>数据权限</li>
     *     <li>接口权限</li>
     * </ul>
     *
     * @param grantPermission grantPermission
     * @return
     */
    boolean grant(GrantPermissionDTO grantPermission);

    /**
     * 获取某个用户的角色
     *
     * @param userId userId
     * @return
     */
    List<SysRole> findRolesByUserId(Long userId);
}
