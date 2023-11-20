package cc.allio.uno.turbo.modules.auth.endpoint;

import cc.allio.uno.turbo.modules.auth.authentication.TurboJwtAuthenticationToken;
import cc.allio.uno.turbo.modules.auth.dto.CaptchaDTO;
import cc.allio.uno.turbo.modules.auth.service.IAuthService;
import cc.allio.uno.turbo.modules.auth.provider.TurboUser;
import cc.allio.uno.turbo.common.exception.BizException;
import cc.allio.uno.turbo.common.util.AuthUtil;
import cc.allio.uno.turbo.common.web.R;
import cc.allio.uno.turbo.common.web.TurboController;
import cc.allio.uno.turbo.modules.system.entity.SysUser;
import cc.allio.uno.turbo.modules.system.vo.SysMenuTreeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Tag(name = "auth")
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
    public R<TurboJwtAuthenticationToken> register(@RequestBody SysUser sysUser) throws BizException {
        TurboJwtAuthenticationToken token = authService.register(sysUser);
        return R.ok(token);
    }

    @GetMapping("/current-user")
    @Operation(summary = "获取当前用户")
    public R<TurboUser> currentUser() {
        return R.ok(AuthUtil.getCurrentUser());
    }

    @GetMapping("/menus")
    @Operation(summary = "获取当前用户菜单")
    public R<List<SysMenuTreeVO>> currentUserMenus() {
        return R.ok(authService.currentUserMenus());
    }

    @GetMapping("/logout")
    @Operation(summary = "退出")
    public R logout() {
        // TODO 后续处理
        return success();
    }

    @GetMapping("/changePassword")
    @Operation(summary = "修改密码")
    public R changePassword(@Validated @Min(6) String newPassword) throws BizException {
        authService.changePassword(newPassword);
        return R.ok();
    }

    @GetMapping("/modify")
    @Operation(summary = "修改用户信息")
    public R modify(@Validated @Min(6) String newPassword) throws BizException {
        authService.changePassword(newPassword);
        return R.ok();
    }
}
