package cc.allio.turbo.modules.auth.oauth2;

import cc.allio.uno.core.StringPool;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.zhyd.oauth.enums.scope.*;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;

import java.util.stream.Collectors;

/**
 * Reference {@link org.springframework.security.config.oauth2.client.CommonOAuth2Provider} idea.
 * <p>implementation turbo oauth2 client </p>
 *
 * @author j.x
 * @date 2024/3/30 16:28
 * @since 0.1.1
 */
@Getter
@AllArgsConstructor
public enum OAuth2Provider {

    /**
     * wechat open platform
     */
    WECHAT_OPEN("wechat-open") {
        @Override
        public ClientRegistration.Builder getBuilder() {
            ClientRegistration.Builder builder = getBuilder(ClientAuthenticationMethod.CLIENT_SECRET_BASIC, DEFAULT_REDIRECT_URL);
            builder.authorizationUri("https://open.weixin.qq.com/connect/qrconnect");
            builder.tokenUri("https://api.weixin.qq.com/sns/oauth2/access_token");
            builder.userInfoUri("https://api.weixin.qq.com/sns/userinfo");
            builder.userNameAttributeName("id");
            builder.clientName("wechat-open");
            return builder;
        }
    },

    /**
     * wechat public platform
     */
    WECHAT_MP("wechat-mp") {
        @Override
        public ClientRegistration.Builder getBuilder() {
            ClientRegistration.Builder builder = getBuilder(ClientAuthenticationMethod.CLIENT_SECRET_BASIC, DEFAULT_REDIRECT_URL);
            String[] scope = combineScope(AuthWechatMpScope.SNSAPI_USERINFO);
            builder.scope(scope);
            builder.authorizationUri("https://open.weixin.qq.com/connect/oauth2/authorize");
            builder.tokenUri("https://api.weixin.qq.com/sns/oauth2/access_token");
            builder.userInfoUri("https://api.weixin.qq.com/sns/userinfo");
            builder.userNameAttributeName("id");
            builder.clientName("wechat-mp");
            return builder;
        }
    },

    /**
     * wechat enterprise qr login
     */
    WECHAT_ENTERPRISE("wechat-enterprise") {
        @Override
        public ClientRegistration.Builder getBuilder() {
            ClientRegistration.Builder builder = getBuilder(ClientAuthenticationMethod.CLIENT_SECRET_BASIC, DEFAULT_REDIRECT_URL);
            builder.authorizationUri("https://open.work.weixin.qq.com/wwopen/sso/qrConnect");
            builder.tokenUri("https://qyapi.weixin.qq.com/cgi-bin/gettoken");
            builder.userInfoUri("https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo");
            builder.clientName("wechat-enterprise");
            return builder;
        }
    },

    /**
     * wechat enterprise qrcode third login
     */
    WECHAT_ENTERPRISE_QR("wechat-enterprise-qr") {
        @Override
        public ClientRegistration.Builder getBuilder() {
            ClientRegistration.Builder builder = getBuilder(ClientAuthenticationMethod.CLIENT_SECRET_BASIC, DEFAULT_REDIRECT_URL);
            builder.authorizationUri("https://open.work.weixin.qq.com/wwopen/sso/3rd_qrConnect");
            builder.tokenUri("https://qyapi.weixin.qq.com/cgi-bin/service/get_provider_token");
            builder.userInfoUri("https://qyapi.weixin.qq.com/cgi-bin/service/get_login_info");
            builder.clientName("wechat-enterprise-qr");
            return builder;
        }
    },

    /**
     * wechat enterprise web login
     */
    WECHAT_ENTERPRISE_WEB("wechat-enterprise-web") {
        @Override
        public ClientRegistration.Builder getBuilder() {
            ClientRegistration.Builder builder = getBuilder(ClientAuthenticationMethod.CLIENT_SECRET_BASIC, DEFAULT_REDIRECT_URL);
            String[] scope = combineScope(AuthWeChatEnterpriseWebScope.SNSAPI_BASE);
            builder.scope(scope);
            builder.authorizationUri("https://open.weixin.qq.com/connect/oauth2/authorize");
            builder.tokenUri("https://qyapi.weixin.qq.com/cgi-bin/gettoken");
            builder.userInfoUri("https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo");
            builder.clientName("wechat-enterprise-qrcode-third");
            return builder;
        }
    },

    WEIBO("weibo") {
        @Override
        public ClientRegistration.Builder getBuilder() {
            ClientRegistration.Builder builder = getBuilder(ClientAuthenticationMethod.CLIENT_SECRET_BASIC, DEFAULT_REDIRECT_URL);
            String[] scope = combineScope(AuthWeiboScope.ALL);
            builder.scope(scope);
            builder.authorizationUri("https://api.weibo.com/oauth2/authorize");
            builder.tokenUri("https://api.weibo.com/oauth2/access_token");
            builder.userInfoUri("https://api.weibo.com/2/users/show.json");
            builder.clientName("weibo");
            return builder;
        }
    },

    GITEE("gitee") {
        @Override
        public ClientRegistration.Builder getBuilder() {
            ClientRegistration.Builder builder = getBuilder(ClientAuthenticationMethod.CLIENT_SECRET_BASIC, DEFAULT_REDIRECT_URL);
            String[] scope = combineScope(AuthGiteeScope.USER_INFO, AuthGiteeScope.EMAILS);
            builder.scope(scope);
            builder.authorizationUri("https://gitee.com/oauth/authorize");
            builder.tokenUri("https://gitee.com/oauth/token");
            builder.userInfoUri("https://gitee.com/api/v5/user");
            builder.userNameAttributeName("id");
            builder.clientName("gitee");
            return builder;
        }
    },

    DINGTALK("dingtalk") {
        @Override
        public ClientRegistration.Builder getBuilder() {
            ClientRegistration.Builder builder = getBuilder(ClientAuthenticationMethod.CLIENT_SECRET_BASIC, DEFAULT_REDIRECT_URL);
            builder.authorizationUri("https://oapi.dingtalk.com/connect/qrconnect");
            builder.userInfoUri("https://oapi.dingtalk.com/sns/getuserinfo_bycode");
            builder.userNameAttributeName("id");
            builder.clientName("dingtalk");
            return builder;
        }
    },

    DINGTALK_ACCOUNT("dingtalk-account") {
        @Override
        public ClientRegistration.Builder getBuilder() {
            ClientRegistration.Builder builder = getBuilder(ClientAuthenticationMethod.CLIENT_SECRET_BASIC, DEFAULT_REDIRECT_URL);
            builder.authorizationUri("https://oapi.dingtalk.com/connect/oauth2/sns_authorize");
            builder.userInfoUri("https://oapi.dingtalk.com/sns/getuserinfo_bycode");
            builder.userNameAttributeName("id");
            builder.clientName("dingtalk-account");
            return builder;
        }
    },

    GOOGLE("google") {
        @Override
        public ClientRegistration.Builder getBuilder() {
            ClientRegistration.Builder builder = getBuilder(ClientAuthenticationMethod.CLIENT_SECRET_BASIC, DEFAULT_REDIRECT_URL);
            builder.scope("openid", "profile", "email");
            builder.authorizationUri("https://accounts.google.com/o/oauth2/v2/auth");
            builder.tokenUri("https://www.googleapis.com/oauth2/v4/token");
            builder.jwkSetUri("https://www.googleapis.com/oauth2/v3/certs");
            builder.issuerUri("https://accounts.google.com");
            builder.userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo");
            builder.userNameAttributeName(IdTokenClaimNames.SUB);
            builder.clientName("google");
            return builder;
        }

    },

    GITHUB("github") {
        @Override
        public ClientRegistration.Builder getBuilder() {
            ClientRegistration.Builder builder = getBuilder(ClientAuthenticationMethod.CLIENT_SECRET_BASIC, DEFAULT_REDIRECT_URL);
            builder.scope("read:user");
            builder.authorizationUri("https://github.com/login/oauth/authorize");
            builder.tokenUri("https://github.com/login/oauth/access_token");
            builder.userInfoUri("https://api.github.com/user");
            builder.userNameAttributeName("id");
            builder.clientName("github");
            return builder;
        }
    },

    FACEBOOK("facebook") {
        @Override
        public ClientRegistration.Builder getBuilder() {
            ClientRegistration.Builder builder = getBuilder(ClientAuthenticationMethod.CLIENT_SECRET_POST, DEFAULT_REDIRECT_URL);
            builder.scope("public_profile", "email");
            builder.authorizationUri("https://www.facebook.com/v2.8/dialog/oauth");
            builder.tokenUri("https://graph.facebook.com/v2.8/oauth/access_token");
            builder.userInfoUri("https://graph.facebook.com/me?fields=id,name,email");
            builder.userNameAttributeName("id");
            builder.clientName("facebook");
            return builder;
        }

    },

    OKTA("okta") {
        @Override
        public ClientRegistration.Builder getBuilder() {
            ClientRegistration.Builder builder = getBuilder(ClientAuthenticationMethod.CLIENT_SECRET_BASIC, DEFAULT_REDIRECT_URL);
            builder.scope("openid", "profile", "email");
            builder.userNameAttributeName(IdTokenClaimNames.SUB);
            builder.clientName("okta");
            return builder;
        }

    };

    private final String registrationId;

    private static final String DEFAULT_REDIRECT_URL = "{baseUrl}/oauth2/code/{action}/{registrationId}";

    protected final ClientRegistration.Builder getBuilder(ClientAuthenticationMethod method, String redirectUri) {
        ClientRegistration.Builder builder = ClientRegistration.withRegistrationId(registrationId);
        builder.clientAuthenticationMethod(method);
        builder.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE);
        builder.redirectUri(redirectUri);
        return builder;
    }

    /**
     * Create a new
     * {@link org.springframework.security.oauth2.client.registration.ClientRegistration.Builder
     * ClientRegistration.Builder} pre-configured with provider defaults.
     *
     * @return a builder instance
     */
    public abstract ClientRegistration.Builder getBuilder();

    /**
     * obtain OAuth2Provider base on registration id
     *
     * @param registrationId registrationId
     * @return a {@link OAuth2Provider} instance or null
     */
    public static OAuth2Provider getByRegistrationId(String registrationId) {
        for (OAuth2Provider oAuth2Provider : values()) {
            if (oAuth2Provider.registrationId.equals(registrationId)) {
                return oAuth2Provider;
            }
        }
        return null;
    }

    /**
     * combine {@link AuthScope} scope name, and split character ','
     *
     * @param scopes scopes
     * @return scope
     */
    public static String[] combineScope(AuthScope... scopes) {
        if (scopes == null || scopes.length == 0) {
            return new String[]{};
        }
        return Lists.newArrayList(scopes)
                .stream()
                .map(AuthScope::getScope)
                .toArray(String[]::new);
    }
}