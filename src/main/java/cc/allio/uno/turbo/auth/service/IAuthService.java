package cc.allio.uno.turbo.auth.service;

import cc.allio.uno.turbo.auth.authentication.TurboJwtAuthenticationToken;
import cc.allio.uno.turbo.auth.dto.CaptchaDTO;
import cc.allio.uno.turbo.common.exception.BizException;
import cc.allio.uno.turbo.system.entity.SysUser;
import cc.allio.uno.turbo.system.vo.SysMenuTreeVO;

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
     * @return
     */
    TurboJwtAuthenticationToken register(SysUser sysUser) throws BizException;

    /**
     * 获取当前用户的
     *
     * @return
     */
    List<SysMenuTreeVO> currentUserMenus();
}
