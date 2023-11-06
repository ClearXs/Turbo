package cc.allio.uno.turbo.auth.endpoint;

import cc.allio.uno.turbo.auth.authentication.TurboJwtAuthenticationToken;
import cc.allio.uno.turbo.auth.dto.CaptchaDTO;
import cc.allio.uno.turbo.auth.service.IAuthService;
import cc.allio.uno.turbo.auth.userdetails.TurboUser;
import cc.allio.uno.turbo.auth.util.JwtUtil;
import cc.allio.uno.turbo.common.R;
import cc.allio.uno.turbo.common.TurboController;
import cc.allio.uno.turbo.system.entity.SysUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Tag(name = "auth模块")
public class AuthEndpoint extends TurboController {

    private IAuthService authService;

    @GetMapping("/captcha")
    @Operation(summary = "获取验证码")
    public R<CaptchaDTO> captcha() {
        CaptchaDTO captcha = authService.captcha();
        return R.ok(captcha);
    }

    @PostMapping("/register")
    @Operation(summary = "注册")
    public R<TurboJwtAuthenticationToken> register(@RequestBody SysUser sysUser) {
        TurboJwtAuthenticationToken token = authService.register(sysUser);
        return R.ok(token);
    }

    @GetMapping("/current-userr")
    @Operation(summary = "获取当前用户")
    public R<TurboUser> currentUser() {
        return JwtUtil.getCurrentUser();
    }
}
