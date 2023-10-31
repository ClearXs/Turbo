package cc.allio.uno.turbo.auth.endpoint;

import cc.allio.uno.core.util.id.IdGenerator;
import cc.allio.uno.turbo.auth.SecureProperties;
import cc.allio.uno.turbo.auth.constant.ExpireAt;
import cc.allio.uno.turbo.auth.dto.CaptchaDTO;
import cc.allio.uno.turbo.common.R;
import cc.allio.uno.turbo.common.TurboController;
import cc.allio.uno.turbo.common.cache.Caches;
import cc.allio.uno.turbo.common.cache.TurboCache;
import io.springboot.captcha.SpecCaptcha;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Tag(name = "auth模块")
public class AuthEndpoint extends TurboController {

    private final SecureProperties secureProperties;

    @GetMapping("/captcha")
    @Operation(summary = "获取验证码")
    public R<CaptchaDTO> captcha() {
        SecureProperties.Captcha captchaSettings = secureProperties.getCaptcha();
        SpecCaptcha captcha = new SpecCaptcha(captchaSettings.getWidth(), captchaSettings.getHeight(), captchaSettings.getLength());
        TurboCache turboCache = Caches.getIfAbsent(Caches.CAPTCHA);
        CaptchaDTO captchaDTO = new CaptchaDTO();
        captchaDTO.setCaptchaId(IdGenerator.defaultGenerator().toHex());
        captchaDTO.setBase64(captcha.toBase64());
        ExpireAt expireAt = captchaSettings.getExpireAt();
        turboCache.setEx(captchaDTO.getCaptchaId(), captcha.text(), expireAt.range(), TimeUnit.MILLISECONDS);
        return R.ok(captchaDTO);
    }
}
