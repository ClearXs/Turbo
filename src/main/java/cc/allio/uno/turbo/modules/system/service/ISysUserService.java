package cc.allio.uno.turbo.modules.system.service;

import cc.allio.uno.turbo.common.exception.BizException;
import cc.allio.uno.turbo.common.mybatis.service.ITurboCrudService;
import cc.allio.uno.turbo.modules.system.dto.BindingRoleDTO;
import cc.allio.uno.turbo.modules.system.entity.SysUser;
import cc.allio.uno.turbo.modules.system.vo.SysUserVO;

public interface ISysUserService extends ITurboCrudService<SysUser> {

    /**
     * 根据用户名获取用户
     *
     * @param username username
     * @return sysuser or null
     */
    SysUserVO findByUsername(String username) throws BizException;

    /**
     * 用户绑定角色
     *
     * @param bindingRole bindingRole
     * @return
     */
    Boolean bindingRoles(BindingRoleDTO bindingRole);
}
