package cc.allio.uno.turbo.auth.service.impl;

import cc.allio.uno.core.util.id.IdGenerator;
import cc.allio.uno.turbo.auth.SecureProperties;
import cc.allio.uno.turbo.auth.authentication.TurboJwtAuthenticationToken;
import cc.allio.uno.turbo.auth.constant.ExpireAt;
import cc.allio.uno.turbo.auth.dto.CaptchaDTO;
import cc.allio.uno.turbo.auth.service.IAuthService;
import cc.allio.uno.turbo.auth.userdetails.TurboUser;
import cc.allio.uno.turbo.auth.util.JwtUtil;
import cc.allio.uno.turbo.common.cache.Caches;
import cc.allio.uno.turbo.common.cache.TurboCache;
import cc.allio.uno.turbo.system.entity.SysUser;
import cc.allio.uno.turbo.system.service.ISysUserService;
import cc.allio.uno.turbo.system.vo.SysUserVO;
import io.springboot.captcha.SpecCaptcha;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final ISysUserService sysUserService;
    private final SecureProperties secureProperties;

    @Override
    public CaptchaDTO captcha() {
        SecureProperties.Captcha captchaSettings = secureProperties.getCaptcha();
        SpecCaptcha captcha = new SpecCaptcha(captchaSettings.getWidth(), captchaSettings.getHeight(), captchaSettings.getLength());
        TurboCache turboCache = Caches.getIfAbsent(Caches.CAPTCHA);
        CaptchaDTO captchaDTO = new CaptchaDTO();
        captchaDTO.setCaptchaId(IdGenerator.defaultGenerator().toHex());
        captchaDTO.setBase64(captcha.toBase64());
        ExpireAt expireAt = captchaSettings.getExpireAt();
        turboCache.setEx(captchaDTO.getCaptchaId(), captcha.text(), expireAt.range(), TimeUnit.MILLISECONDS);
        return captchaDTO;
    }

    @Override
    @Transactional
    public TurboJwtAuthenticationToken register(SysUser sysUser) {
        // 1.存入数据库
        sysUserService.save(sysUser);
        // 2.获取新创建用户详细
        SysUserVO user = sysUserService.findByUsername(sysUser.getUsername());
        // 3.生成jwt
        return JwtUtil.encode(new TurboUser(user));
    }
}
