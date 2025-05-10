package cc.allio.turbo.modules.auth.web.websocket;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

/**
 * add {@link Authentication} in websocket
 *
 * @author j.x
 * @since 0.2.0
 */
public class AuthenticationWebSocketHandler extends AbstractWebSocketHandler {

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        injectAuthentication(session);
        super.afterConnectionEstablished(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        injectAuthentication(session);
        super.handleMessage(session, message);
    }

    void injectAuthentication(WebSocketSession session) {
        Authentication principal = (Authentication) session.getPrincipal();
        SecurityContext context = securityContextHolderStrategy.getContext();
        if (context == null) {
            context = securityContextHolderStrategy.createEmptyContext();
        }
        context.setAuthentication(principal);
        securityContextHolderStrategy.setContext(context);
    }
}
