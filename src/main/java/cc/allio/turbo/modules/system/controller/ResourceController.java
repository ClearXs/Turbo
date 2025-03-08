package cc.allio.turbo.modules.system.controller;

import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.common.web.R;
import cc.allio.turbo.modules.auth.authentication.TurboJwtAuthenticationToken;
import cc.allio.turbo.modules.auth.jwt.JwtAuthentication;
import cc.allio.turbo.modules.auth.provider.TurboUser;
import cc.allio.turbo.modules.system.domain.SysUserVO;
import cc.allio.turbo.modules.system.service.ISysUserService;
import com.google.common.collect.Sets;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@RestController
@RequestMapping("/sys/resource")
@Tag(name = "系统资源")
public class ResourceController {

    @Value("${turbo.secure.cipher}")
    private String cipher;

    @Autowired
    private JwtAuthentication jwtAuthentication;

    @Autowired
    private ISysUserService userService;

    @GetMapping("/permanent-token")
    public R<String> getPermanentToken(@RequestParam String secret) {
        if (!cipher.equals(secret)) {
            return R.internalError("incorrect secret " + secret);
        }
        String token = generatePermanentToken();
        return R.ok(token);
    }

    String generatePermanentToken() {
        Instant expired =
                new Date().toInstant().plus(10000, ChronoUnit.DAYS);
        try {
            SysUserVO user = userService.findByUsername("admin");
            TurboUser turboUser = new TurboUser(user, Sets.newHashSet());
            TurboJwtAuthenticationToken token = jwtAuthentication.encode(turboUser, expired);
            return token.getToken().getTokenValue();
        } catch (BizException e) {
            throw new RuntimeException(e);
        }
    }

}
