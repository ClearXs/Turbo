package cc.allio.uno.turbo.modules.auth.service;

import cc.allio.uno.turbo.modules.auth.authentication.TurboJwtAuthenticationToken;
import cc.allio.uno.turbo.modules.auth.dto.CaptchaDTO;
import cc.allio.uno.turbo.common.exception.BizException;
import cc.allio.uno.turbo.modules.auth.provider.TurboUser;
import cc.allio.uno.turbo.modules.system.entity.SysUser;
import cc.allio.uno.turbo.modules.system.vo.SysMenuTree;

import java.util.List;

public interface IAuthService {

    /**
     * 生成验证码
     *
     * @return
     */
    CaptchaDTO captcha();

    /**
     * 注册账号
     *
     * @param sysUser sysUser 实体
     * @return 生成的jwt token
     */
    TurboJwtAuthenticationToken register(SysUser sysUser) throws BizException;

    /**
     * 获取当前用户的
     *
     * @return
     */
    List<SysMenuTree> currentUserMenus();

    /**
     * 更改密码
     *
     * @param newPassword 新密码
     * @return 重新生成的jwt token
     */
    TurboJwtAuthenticationToken changePassword(String newPassword) throws BizException;

    /**
     * 修改个人信息
     *
     * @param user user
     * @return 重新生成的jwt token
     */
    TurboJwtAuthenticationToken modify(TurboUser user) throws BizException;

    /**
     * 通用系统的加密算法对密码加密
     *
     * @param rawPassword 明文密码
     * @return 密文
     */
    String encryptPassword(String rawPassword);
}
