package cc.allio.uno.turbo.modules.system.service;

import cc.allio.uno.turbo.common.exception.BizException;
import cc.allio.uno.turbo.common.mybatis.service.ITurboCrudService;
import cc.allio.uno.turbo.common.web.params.QueryParam;
import cc.allio.uno.turbo.modules.system.dto.BindingOrgDTO;
import cc.allio.uno.turbo.modules.system.dto.BindingRoleDTO;
import cc.allio.uno.turbo.modules.system.entity.SysUser;
import cc.allio.uno.turbo.modules.system.vo.SysUserVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

public interface ISysUserService extends ITurboCrudService<SysUser> {

    /**
     * 自定义用户列表查询
     *
     * @param param param
     */
    List<SysUser> findList(QueryParam<SysUser> param);

    /**
     * 自定义用户分页查询
     *
     * @param param param
     */
    IPage<SysUser> findPage(QueryParam<SysUser> param);

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

    /**
     * 更改密码
     *
     * @param userId      用户id
     * @param newPassword 新密码
     * @return
     */
    Boolean changePassword(Long userId, String newPassword);

    /**
     * 绑定组织
     *
     * @param bindingOrg bindingOrg
     */
    Boolean bingdingOrgs(BindingOrgDTO bindingOrg);

    /**
     * 获取未绑定组织的用户列表
     */
    List<SysUser> unboundOrgUser();
}
