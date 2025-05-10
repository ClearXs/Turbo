package cc.allio.turbo.modules.ai.chat.memory;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;

import java.util.function.Supplier;

public abstract class AuthenticationSessionChatMemory extends SessionChatMemory {

    protected final Authentication authentication;

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();

    protected AuthenticationSessionChatMemory(Authentication authentication, String sessionId) {
        super(sessionId);
        this.authentication = authentication;
    }

    /**
     * in {@link Authentication} execution.
     *
     * @param <T> the return type
     * @return
     */
    protected <T> T aroundAuthentication(Supplier<T> executor) {
        SecurityContext context = securityContextHolderStrategy.getContext();
        if (context == null) {
            context = securityContextHolderStrategy.createEmptyContext();
        }
        context.setAuthentication(authentication);
        try {
            return executor.get();
        } finally {
            context.setAuthentication(null);
        }
    }
}
