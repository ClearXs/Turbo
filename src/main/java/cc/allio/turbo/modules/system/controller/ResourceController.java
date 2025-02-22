package cc.allio.turbo.modules.system.controller;

import cc.allio.turbo.common.constant.Secures;
import cc.allio.turbo.common.web.R;
import cc.allio.turbo.modules.auth.jwt.TurboJwtEncoder;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
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

    @GetMapping("/permanent-token")
    public R<String> getPermanentToken(@RequestParam String secret) {
        if (!cipher.equals(secret)) {
            return R.internalError("incorrect secret " + secret);
        }
        String token = generatePermanentToken(secret);
        return R.ok(token);
    }

    String generatePermanentToken(String secret) {
        Instant expired =
                new Date().toInstant().plus(10000, ChronoUnit.DAYS);
        JwtClaimsSet jwtClaimsSet =
                JwtClaimsSet.builder()
                        .expiresAt(expired)
                        .claim(Secures.CIPHER, secret)
                        .build();
        Jwt jwt =
                TurboJwtEncoder.getInstance().encode(JwtEncoderParameters.from(jwtClaimsSet));
        return jwt.getTokenValue();
    }
}
