package cc.allio.turbo.modules.auth.oauth2;

import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.core.util.ObjectUtils;
import com.google.common.collect.Lists;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * wrapper {@link ClientRegistrationRepository} by {@link InMemoryClientRegistrationRepository}.
 * <p>built in through {@link OAuth2Provider} and {@link OAuth2ClientProperties#getRegistration()} mapping</p>
 *
 * @author j.x
 * @date 2024/3/30 16:31
 * @since 0.1.1
 */
public class TurboClientRegistrationRepository implements ClientRegistrationRepository {

    private final ClientRegistrationRepository internal;

    public TurboClientRegistrationRepository(TurboOAuth2ClientProperties oAuth2ClientProperties) {
        Map<String, OAuth2ClientProperties.Registration> propertiesRegistrations = oAuth2ClientProperties.getRegistration();
        List<ClientRegistration> registrations = Lists.newArrayList();
        for (Map.Entry<String, OAuth2ClientProperties.Registration> entryForRegistration : propertiesRegistrations.entrySet()) {
            String registrationId = entryForRegistration.getKey();
            OAuth2Provider oAuth2Provider = OAuth2Provider.getByRegistrationId(registrationId);
            if (oAuth2Provider != null) {
                ClientRegistration.Builder builder = oAuth2Provider.getBuilder();
                OAuth2ClientProperties.Registration properties = entryForRegistration.getValue();
                // client id
                mapping(builder::clientId, properties::getClientId);
                // client name
                mapping(builder::clientName, properties::getClientName);
                // client secret
                mapping(builder::clientSecret, properties::getClientSecret);
                // scope
                mapping(builder::scope, properties::getScope);
                // redirec uri
                mapping(builder::redirectUri, properties::getRedirectUri);
                // build
                ClientRegistration registration = builder.build();
                registrations.add(registration);
            }
        }

        if (CollectionUtils.isNotEmpty(registrations)) {
            this.internal = new InMemoryClientRegistrationRepository(registrations);
        } else {
            this.internal = null;
        }
    }

    @Override
    public ClientRegistration findByRegistrationId(String registrationId) {
        return internal.findByRegistrationId(registrationId);
    }

    /**
     * from supplier give in acceptor if supplier not empty
     *
     * @param acceptor the acceptor
     * @param supplier the supplier
     * @param <T>      ele type
     */
    <T> void mapping(Consumer<T> acceptor, Supplier<T> supplier) {
        T ele = supplier.get();
        if (ObjectUtils.isNotEmpty(ele)) {
            acceptor.accept(ele);
        }
    }
}
