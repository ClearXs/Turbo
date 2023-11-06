package cc.allio.uno.turbo.system.service;

import cc.allio.uno.turbo.common.exception.BizException;
import cc.allio.uno.turbo.system.entity.SysUser;
import cc.allio.uno.turbo.system.vo.SysUserVO;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ISysUserService extends IService<SysUser> {

    /**
     * 保存用户
     *
     * @param sysUser
     * @return
     */
    boolean saveUser(SysUser sysUser) throws BizException;

    /**
     * 根据用户名获取用户
     *
     * @param username username
     * @return sysuser or null
     */
    SysUserVO findByUsername(String username);
}
