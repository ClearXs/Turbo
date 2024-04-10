package cc.allio.turbo.modules.auth.oauth2.extractor;

import cc.allio.turbo.modules.auth.oauth2.OAuth2Provider;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

/**
 * wechat enterprise web login
 *
 * @author j.x
 * @date 2024/4/10 19:50
 * @since 0.1.1
 */
@Component
public class WechatEnterpriseWebExtractor implements OAuth2UserExtractor {

    @Override
    public String withUUID(OAuth2User oAuth2User) {
        return oAuth2User.getAttribute("UserId");
    }

    @Override
    public String withUsername(OAuth2User oAuth2User) {
        return oAuth2User.getAttribute("name");
    }

    @Override
    public String withEmail(OAuth2User oAuth2User) {
        return oAuth2User.getAttribute("email");
    }

    @Override
    public String getRegistrationId() {
        return OAuth2Provider.WECHAT_ENTERPRISE_WEB.getRegistrationId();
    }
}
