package cc.allio.turbo.modules.auth.oauth2.extractor;

import cc.allio.turbo.modules.auth.oauth2.OAuth2Provider;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * extract {@link OAuth2User} to sys user
 *
 * @author j.x
 * @date 2024/3/30 17:48
 * @since 0.1.1
 */
public interface OAuth2UserExtractor {

    /**
     * extract to unique ID
     *
     * @return UUID
     */
    String withUUID(OAuth2User oAuth2User);

    /**
     * extract to username
     *
     * @return username
     */
    String withUsername(OAuth2User oAuth2User);

    /**
     * extract to email if exist
     *
     * @return email
     */
    String withEmail(OAuth2User oAuth2User);

    /**
     * extract to nickname if exist
     *
     * @return nickname
     */
    String withNickname(OAuth2User oAuth2User);

    /**
     * relational third system
     *
     * @return a {@link OAuth2Provider} instance
     */
    String getRegistrationId();
}
