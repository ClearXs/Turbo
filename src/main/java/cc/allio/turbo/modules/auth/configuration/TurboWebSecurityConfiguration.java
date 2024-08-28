package cc.allio.turbo.modules.auth.configuration;

import cc.allio.turbo.modules.auth.jwt.JwtAuthentication;
import cc.allio.turbo.modules.auth.web.TurboUserArgumentResolver;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@AllArgsConstructor
public class TurboWebSecurityConfiguration implements WebMvcConfigurer {

    private final JwtAuthentication jwtAuthentication;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new TurboUserArgumentResolver(jwtAuthentication));
    }
}
