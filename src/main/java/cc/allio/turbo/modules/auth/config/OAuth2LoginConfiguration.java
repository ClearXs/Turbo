package cc.allio.turbo.modules.auth.config;

import cc.allio.turbo.modules.auth.oauth2.*;
import cc.allio.turbo.modules.auth.oauth2.extractor.OAuth2UserExtractor;
import cc.allio.turbo.modules.auth.service.IAuthService;
import cc.allio.turbo.modules.system.service.ISysThirdUserService;
import cc.allio.turbo.modules.system.service.ISysUserService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthenticatedPrincipalOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * obtains third system authorized code uri such as: /oauth2/authorization/{registrationId}
 * </p>
 *
 * @see OAuth2AuthorizationRequestRedirectFilter
 * @see OAuth2LoginAuthenticationFilter
 */
@Configuration
@EnableConfigurationProperties(TurboOAuth2ClientProperties.class)
public class OAuth2LoginConfiguration {

    @Bean
    @Order(0)
    public SecurityFilterChain oauth2loginFilterChain(HttpSecurity http,
                                                      OAuth2TokenGenerator oAuth2TokenGenerator,
                                                      TurboOAuth2ClientProperties oAuth2ClientProperties) throws Exception {
        http.authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/oauth2/**")
                                .authenticated()
                )
                .securityMatcher("/oauth2/**") // only match prefix path /oauth2
                .oauth2Login(oauth2 ->
                        // third system callback uri such as: /oauth2/code/login/github?code=xxx&state=xxx
                        oauth2.loginProcessingUrl("/oauth2/code/login/*")
                                .failureHandler(new OAuth2LoginFailureHandler())
                                .successHandler(new OAuth2LoginSuccessHandler(oAuth2TokenGenerator, oAuth2ClientProperties))
                                .authorizationEndpoint(e ->
                                        e.authorizationRequestRepository(new TenantSessionAuthorizationRequestRepository())
                                                .authorizationRedirectStrategy(new CORSAuthorizationRedirectStrategy()))
                )
                .sessionManagement(sessionManager -> {
                    sessionManager.sessionCreationPolicy(SessionCreationPolicy.NEVER);// 禁止用session来进行认证
                });
        return http.build();
    }


    @Bean
    public OAuth2TokenGenerator oAuth2TokenGenerator(ISysUserService sysUserService,
                                                     ISysThirdUserService sysThirdUserService,
                                                     IAuthService authService,
                                                     ObjectProvider<List<OAuth2UserExtractor>> extractorList) {
        List<OAuth2UserExtractor> extractors = extractorList.getIfAvailable(Collections::emptyList);
        Map<String, OAuth2UserExtractor> extractorMap = extractors.stream().collect(Collectors.toMap(OAuth2UserExtractor::getRegistrationId, o -> o));
        return new OAuth2TokenGenerator(sysUserService, sysThirdUserService, authService, extractorMap);
    }

    @Bean
    public ClientRegistrationRepository turboClientRegistrationRepository(TurboOAuth2ClientProperties oAuth2ClientProperties) {
        return new TurboClientRegistrationRepository(oAuth2ClientProperties);
    }

    @Bean
    public OAuth2AuthorizedClientService authorizedClientService(
            ClientRegistrationRepository clientRegistrationRepository) {
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
    }

    @Bean
    public OAuth2AuthorizedClientRepository authorizedClientRepository(
            OAuth2AuthorizedClientService authorizedClientService) {
        return new AuthenticatedPrincipalOAuth2AuthorizedClientRepository(authorizedClientService);
    }
}
