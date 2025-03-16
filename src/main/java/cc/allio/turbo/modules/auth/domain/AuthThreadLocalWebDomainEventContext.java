package cc.allio.turbo.modules.auth.domain;

import cc.allio.turbo.common.domain.DomainEventContext;
import cc.allio.turbo.common.domain.ThreadLocalWebDomainEventContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;

/**
 * add {@link Authentication} in {@link ThreadLocalWebDomainEventContext}
 *
 * @author j.x
 * @since 0.2.0
 */
public class AuthThreadLocalWebDomainEventContext extends ThreadLocalWebDomainEventContext {

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();

    public AuthThreadLocalWebDomainEventContext(DomainEventContext domainEventContext, Authentication authentication) {
        super(domainEventContext);
        SecurityContext context = securityContextHolderStrategy.getContext();
        if (context == null) {
            context = securityContextHolderStrategy.createEmptyContext();
        }
        context.setAuthentication(authentication);
        securityContextHolderStrategy.setContext(context);
    }

}
