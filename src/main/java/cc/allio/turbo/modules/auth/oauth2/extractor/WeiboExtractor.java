package cc.allio.turbo.modules.auth.oauth2.extractor;

import cc.allio.turbo.modules.auth.oauth2.OAuth2Provider;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

/**
 * weibo
 * <p><a href="https://open.weibo.com/wiki/Oauth2/authorize#_loginLayer_1712747042270">api</a></p>
 *
 * @author j.x
 * @date 2024/4/10 19:05
 * @since 0.1.1
 */
@Component
public class WeiboExtractor implements OAuth2UserExtractor {

    @Override
    public String withUUID(OAuth2User oAuth2User) {
        return oAuth2User.getAttribute("id");
    }

    @Override
    public String withUsername(OAuth2User oAuth2User) {
        return oAuth2User.getAttribute("name");
    }

    @Override
    public String withEmail(OAuth2User oAuth2User) {
        return "";
    }

    @Override
    public String getRegistrationId() {
        return OAuth2Provider.WEIBO.getRegistrationId();
    }
}
