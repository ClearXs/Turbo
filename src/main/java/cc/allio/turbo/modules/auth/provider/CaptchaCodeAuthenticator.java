package cc.allio.turbo.modules.auth.provider;

import cc.allio.turbo.common.cache.CacheHelper;
import cc.allio.turbo.common.cache.TurboCache;
import cc.allio.turbo.modules.auth.exception.CaptchaError;
import cc.allio.turbo.modules.auth.exception.CaptchaExpiredException;
import cc.allio.turbo.modules.auth.params.CaptchaCode;
import cc.allio.turbo.modules.auth.params.LoginCaptcha;
import cc.allio.uno.core.util.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * base on {@link cc.allio.turbo.modules.auth.params.LoginMode#CAPTCHA_CODE} authentication.
 *
 * @author j.x
 * @date 2024/4/6 17:45
 * @since 0.1.1
 */
public class CaptchaCodeAuthenticator implements LoginClaimAuthenticator<LoginCaptcha> {

    @Override
    public UsernamePasswordAuthenticationToken authenticate(LoginCaptcha loginClaim) {
        TurboCache cache = CacheHelper.getIfAbsent(CacheHelper.CAPTCHA);
        CaptchaCode captchaCode = loginClaim.getCaptchaCode();
        if (captchaCode == null) {
            throw new AuthenticationServiceException("login mode 'CAPTCHA CODE' captcha content is null");
        }
        String captchaId = captchaCode.getCaptchaId();
        // 判断是否存在
        if (StringUtils.isBlank(captchaId) || !cache.hasKey(captchaId)) {
            throw new CaptchaExpiredException();
        }
        String cacheContent = cache.get(captchaId, String.class);
        // 验证码内容
        String captcha = captchaCode.getCaptcha();
        // 获取大小写，验证验证码内容
        if (!captcha.equalsIgnoreCase(cacheContent)) {
            throw new CaptchaError();
        }
        return new UsernamePasswordAuthenticationToken(loginClaim.getUsername(), loginClaim.getPassword());
    }
}
