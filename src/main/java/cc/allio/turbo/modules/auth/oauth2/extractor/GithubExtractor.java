package cc.allio.turbo.modules.auth.oauth2.extractor;

import cc.allio.turbo.modules.auth.oauth2.OAuth2Provider;
import cc.allio.uno.core.StringPool;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

/**
 * github
 *
 * @author j.x
 * @date 2024/3/30 17:49
 * @since 0.1.1
 */
@Component
public class GithubExtractor implements OAuth2UserExtractor {

    @Override
    public String withUUID(OAuth2User oAuth2User) {
        Object id = oAuth2User.getAttribute("id");
        if (id == null) {
            return StringPool.EMPTY;
        }
        return id.toString();
    }


    @Override
    public String withUsername(OAuth2User oAuth2User) {
        Object username = oAuth2User.getAttribute("name");
        if (username == null) {
            return StringPool.EMPTY;
        }
        return username.toString();
    }

    @Override
    public String withEmail(OAuth2User oAuth2User) {
        Object email = oAuth2User.getAttribute("email");
        if (email == null) {
            return StringPool.EMPTY;
        }
        return email.toString();
    }

    @Override
    public String withNickname(OAuth2User oAuth2User) {
        return oAuth2User.getAttribute("nickname");
    }

    @Override
    public String getRegistrationId() {
        return OAuth2Provider.GITHUB.getRegistrationId();
    }
}
