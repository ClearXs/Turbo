package cc.allio.turbo.modules.auth.oauth2.extractor;

import cc.allio.turbo.modules.auth.oauth2.OAuth2Provider;
import cc.allio.uno.core.StringPool;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * gitee
 *
 * @author j.x
 * @date 2024/4/10 19:12
 * @since 0.1.1
 */
@Component
public class GiteeExtractor implements OAuth2UserExtractor {

    @Override
    public String withUUID(OAuth2User oAuth2User) {
        return Optional.ofNullable(oAuth2User.getAttribute("id"))
                .map(Object::toString)
                .orElse(StringPool.EMPTY);
    }

    @Override
    public String withUsername(OAuth2User oAuth2User) {
        return oAuth2User.getAttribute("login");
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
        return OAuth2Provider.GITEE.getRegistrationId();
    }
}
