package cc.allio.uno.turbo.modules.auth.service;

import cc.allio.uno.turbo.modules.auth.authentication.TurboJwtAuthenticationToken;
import cc.allio.uno.turbo.modules.auth.dto.CaptchaDTO;
import cc.allio.uno.turbo.common.exception.BizException;
import cc.allio.uno.turbo.modules.system.entity.SysUser;
import cc.allio.uno.turbo.modules.system.vo.SysMenuTreeVO;

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
    List<SysMenuTreeVO> currentUserMenus();

    /**
     * 更改密码
     *
     * @param newPassword 新密码
     * @return 重新生成的jwt token
     */
    TurboJwtAuthenticationToken changePassword(String newPassword) throws BizException;
}
