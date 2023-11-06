package cc.allio.uno.turbo.auth.service;

import cc.allio.uno.turbo.auth.authentication.TurboJwtAuthenticationToken;
import cc.allio.uno.turbo.auth.dto.CaptchaDTO;
import cc.allio.uno.turbo.system.entity.SysUser;

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
    TurboJwtAuthenticationToken register(SysUser sysUser);
}
