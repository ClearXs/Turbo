package cc.allio.turbo.modules.auth.oauth2.extractor;

import cc.allio.turbo.modules.auth.oauth2.OAuth2Provider;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * wechat open
 *
 * @author j.x
 * @date 2024/4/10 19:08
 * @since 0.1.1
 */
public class WechatOpenExtractor implements OAuth2UserExtractor {

    @Override
    public String withUUID(OAuth2User oAuth2User) {
        return "";
    }

    @Override
    public String withUsername(OAuth2User oAuth2User) {
        return "";
    }

    @Override
    public String withEmail(OAuth2User oAuth2User) {
        return "";
    }

    @Override
    public String getRegistrationId() {
        return OAuth2Provider.WECHAT_OPEN.getRegistrationId();
    }
}
