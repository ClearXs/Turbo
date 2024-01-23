package cc.allio.turbo.modules.auth.config;

import cc.allio.turbo.modules.auth.exception.AccessDeniedExceptionHandler;
import cc.allio.turbo.modules.auth.exception.AuthenticationExceptionHandler;
import cc.allio.turbo.modules.auth.exception.Oauth2AuthenticationExceptionHandler;
import cc.allio.uno.core.util.IoUtils;
import cc.allio.uno.core.util.JsonUtils;
import cc.allio.turbo.common.web.R;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Configuration
public class OAuth2Configuration {

    @Bean
    @Order(1)
    public SecurityFilterChain authFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                new OAuth2AuthorizationServerConfigurer();
        http.with(authorizationServerConfigurer, c ->
                c.oidc(Customizer.withDefaults())
                        // /oauth2/token 验证
                        .tokenEndpoint(token -> token.errorResponseHandler(new Oauth2AuthenticationExceptionHandler()))
                        // /oauth2/authorize 获取授权码
                        .authorizationEndpoint(authorization ->
                                authorization.errorResponseHandler(new Oauth2AuthenticationExceptionHandler())
                                        .authorizationResponseHandler((request, response, authentication) -> {
                                            R<Authentication> ok = R.ok(authentication);
                                            response.setStatus(ok.getCode());
                                            IoUtils.write(JsonUtils.toJson(ok), response.getOutputStream(), StandardCharsets.UTF_8);
                                        })
                                        .authenticationProvider(new AuthenticationProvider() {
                                            @Override
                                            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                                                return authentication;
                                            }

                                            @Override
                                            public boolean supports(Class<?> authentication) {
                                                return false;
                                            }
                                        })));
        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();
        return http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(ex ->
                        // 异常处理器
                        ex.authenticationEntryPoint(new AuthenticationExceptionHandler())
                                .accessDeniedHandler(new AccessDeniedExceptionHandler()))
                .securityMatcher(endpointsMatcher)
                .build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("turbo-client")
                .clientSecret("{noop}secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/oidc-client")
                .postLogoutRedirectUri("http://127.0.0.1:8080/")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .build();
        return new InMemoryRegisteredClientRepository(oidcClient);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }
}
