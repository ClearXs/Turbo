package cc.allio.uno.turbo.auth.config;

import cc.allio.uno.turbo.auth.SecureProperties;
import cc.allio.uno.turbo.auth.provider.TurboJwtAuthenticationProvider;
import cc.allio.uno.turbo.auth.provider.TurboPasswordEncoder;
import cc.allio.uno.turbo.auth.provider.TurboUserServiceDetails;
import cc.allio.uno.turbo.auth.exception.AccessDeniedExceptionHandler;
import cc.allio.uno.turbo.auth.exception.AuthenticationExceptionHandler;
import cc.allio.uno.turbo.auth.filter.JwtTokenFilter;
import cc.allio.uno.turbo.auth.handler.JwtAuthenticationSuccessHandler;
import cc.allio.uno.turbo.system.service.ISysUserService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableConfigurationProperties(SecureProperties.class)
public class SecurityConfig {

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,
                                                          SecureProperties secureProperties,
                                                          TurboJwtAuthenticationProvider turboJwtAuthenticationProvider,
                                                          JwtTokenFilter jwtTokenFilter) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                        // 忽略放行请求路径
                        authorize.requestMatchers(secureProperties.getSkipList().toArray(String[]::new))
                                .permitAll()
                                // 其他请求需要进行验证
                                .anyRequest()
                                .authenticated())
                // jwt token验证
                .addFilterBefore(jwtTokenFilter, AuthorizationFilter.class)
                .exceptionHandling(ex ->
                        // 异常处理器
                        ex.authenticationEntryPoint(new AuthenticationExceptionHandler())
                                .accessDeniedHandler(new AccessDeniedExceptionHandler()))
                // 使过滤器包含UsernamePasswordAuthenticationFilter，提供login接口
                // 默认使用DaoAuthenticationProvider，调用TurboUserServiceDetails#loadUserByUsername方法
                // AuthenticationManager -> AuthenticationProvider -> UserDetailsService
                .formLogin(login ->
                        // 登陆页面
                        login.loginPage("/login")
                                // 登陆失败跳转的地址
                                .failureUrl("/login")
                                // 认证成功后的回调
                                .successHandler(new JwtAuthenticationSuccessHandler()))
                .authenticationProvider(turboJwtAuthenticationProvider)
                .sessionManagement(sessionManager -> {
                    sessionManager.sessionCreationPolicy(SessionCreationPolicy.NEVER);// 禁止用session来进行认证
                })
                .build();
    }

    @Bean
    public TurboUserServiceDetails turboUserServiceDetails(ISysUserService userService) {
        return new TurboUserServiceDetails(userService);
    }

    @Bean
    public TurboPasswordEncoder turboPasswordEncoder(SecureProperties secureProperties) {
        return new TurboPasswordEncoder(secureProperties);
    }

    @Bean
    public TurboJwtAuthenticationProvider turboJwtAuthenticationProvider(UserDetailsService userDetailsService,
                                                                         PasswordEncoder passwordEncoder,
                                                                         JwtEncoder jwtEncoder,
                                                                         SecureProperties secureProperties) {
        return new TurboJwtAuthenticationProvider(userDetailsService, passwordEncoder, jwtEncoder, secureProperties);
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter(JwtDecoder jwtDecoder) {
        return new JwtTokenFilter(jwtDecoder);
    }

}
