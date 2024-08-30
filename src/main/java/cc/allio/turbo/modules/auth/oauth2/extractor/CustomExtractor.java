package cc.allio.turbo.modules.auth.oauth2.extractor;

import cc.allio.turbo.modules.auth.oauth2.OAuth2Provider;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

/**
 * customize
 *
 * @author j.x
 * @date 2024/8/29 14:49
 * @since 0.1.1
 */
@Component
public class CustomExtractor implements OAuth2UserExtractor {

    @Override
    public String withUUID(OAuth2User oAuth2User) {
        return oAuth2User.getAttribute("userId");
    }

    @Override
    public String withUsername(OAuth2User oAuth2User) {
        return oAuth2User.getAttribute("username");
    }

    @Override
    public String withEmail(OAuth2User oAuth2User) {
        return oAuth2User.getAttribute("email");
    }

    @Override
    public String withNickname(OAuth2User oAuth2User) {
        return oAuth2User.getAttribute("nickname");
    }

    @Override
    public String getRegistrationId() {
        return OAuth2Provider.CUSTOM.getRegistrationId();
    }
}
