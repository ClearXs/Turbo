package cc.allio.turbo.modules.auth.config;

import cc.allio.turbo.modules.auth.exception.AccessDeniedExceptionHandler;
import cc.allio.turbo.modules.auth.exception.AuthenticationExceptionHandler;
import cc.allio.turbo.modules.auth.filter.JwtTokenFilter;
import cc.allio.turbo.modules.auth.handler.JwtAuthenticationSuccessHandler;
import cc.allio.turbo.modules.auth.properties.SecureProperties;
import cc.allio.turbo.modules.auth.provider.TurboJwtAuthenticationProvider;
import cc.allio.turbo.modules.auth.provider.TurboPasswordEncoder;
import cc.allio.turbo.modules.auth.provider.TurboUserDetailsService;
import cc.allio.turbo.modules.system.service.ISysUserService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableConfigurationProperties(SecureProperties.class)
public class SecurityConfiguration {

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,
                                                          SecureProperties secureProperties,
                                                          AuthenticationExceptionHandler authenticationExceptionHandler,
                                                          ISysUserService userService) throws Exception {
        TurboUserDetailsService userDetailsService = new TurboUserDetailsService(userService);
        TurboPasswordEncoder passwordEncoder = new TurboPasswordEncoder(secureProperties);
        TurboJwtAuthenticationProvider jwtAuthenticationProvider = new TurboJwtAuthenticationProvider(userDetailsService, passwordEncoder);
        JwtTokenFilter jwtTokenFilter = new JwtTokenFilter();
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
                        ex.authenticationEntryPoint(authenticationExceptionHandler)
                                .accessDeniedHandler(new AccessDeniedExceptionHandler()))
                // 使过滤器包含UsernamePasswordAuthenticationFilter，提供login接口
                // 默认使用DaoAuthenticationProvider，调用TurboUserServiceDetails#loadUserByUsername方法
                // AuthenticationManager -> AuthenticationProvider -> UserDetailsService
                .formLogin(login ->
                        // 登陆页面
                        login.loginPage("/auth/login")
                                // 当存在failureHandler实例时，不走重定向的逻辑，而是选择走自定义的失败处理器
                                .failureHandler(authenticationExceptionHandler)
                                // 认证成功后的回调
                                .successHandler(new JwtAuthenticationSuccessHandler()))
                .authenticationProvider(jwtAuthenticationProvider)
                .sessionManagement(sessionManager -> {
                    sessionManager.sessionCreationPolicy(SessionCreationPolicy.NEVER);// 禁止用session来进行认证
                })
                .build();
    }

    @Bean
    public AuthenticationExceptionHandler authenticationExceptionHandler() {
        return new AuthenticationExceptionHandler();
    }

}
