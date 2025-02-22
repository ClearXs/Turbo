package cc.allio.turbo.modules.ai.websocket;

import cc.allio.turbo.common.domain.DomainEventContext;
import cc.allio.turbo.common.domain.GeneralDomain;
import cc.allio.turbo.common.domain.Subscription;
import cc.allio.turbo.common.util.WebUtil;
import cc.allio.turbo.modules.ai.Driver;
import cc.allio.turbo.modules.ai.Input;
import cc.allio.turbo.modules.ai.Output;
import cc.allio.turbo.modules.ai.Topics;
import cc.allio.turbo.modules.ai.model.ModelOptions;
import cc.allio.turbo.modules.ai.runtime.Variable;
import cc.allio.uno.core.bus.TopicKey;
import cc.allio.uno.core.util.DateUtil;
import cc.allio.uno.core.util.JsonUtils;
import cc.allio.uno.core.util.id.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.util.Optionals;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import reactor.core.Disposable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

/**
 * websocket for chat handler.
 *
 * @author j.x
 * @since 0.2.0
 */
@Slf4j
public class ChatHandler extends TextWebSocketHandler implements InitializingBean, DisposableBean {

    private final Driver<Input> inputDriver;
    private final Driver<Output> outputDriver;
    private final JwtDecoder jwtDecoder;

    private Disposable disposable;

    public ChatHandler(Driver<Input> inputDriver,
                       Driver<Output> outputDriver,
                       JwtDecoder jwtDecoder) {
        this.inputDriver = inputDriver;
        this.outputDriver = outputDriver;
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.disposable =
                outputDriver.subscribeOn(Topics.OUTPUT_PATTERNS)
                        .observeMany()
                        .doOnNext(this::handleOutput)
                        .subscribe();
    }

    void handleOutput(Subscription<Output> subscription) {
        subscription.getDomain()
                .ifPresent(output -> {
                    Input input = output.getInput();
                    Optional.ofNullable(input)
                            .flatMap(i -> {
                                if (i instanceof WsInput wsInput) {
                                    return Optional.ofNullable(wsInput.getSession());
                                }
                                return Optional.empty();
                            })
                            .ifPresent(session -> {
                                String message = output.getMessage();
                                TextMessage textMessage = new TextMessage(message.getBytes(StandardCharsets.UTF_8));
                                try {
                                    session.sendMessage(textMessage);
                                } catch (IOException ex) {
                                    log.error("failed ws send msg: {}", message, ex);
                                }
                            });
                });
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("receive message: {}", message.getPayload());
        }
        Message msg = null;
        try {
            msg = JsonUtils.parse(message.getPayload(), Message.class);
        } catch (Exception ex) {
            // ignore
            // user send msg is literal string
        }
        WsInput input = new WsInput();
        String sessionId = session.getId();
        input.setId(IdGenerator.defaultGenerator().getNextId());
        input.setSession(session);
        input.setSessionId(sessionId);
        input.setVariable(msg == null ? new Variable() : msg.getVariable());
        input.setAgents(msg == null ? Set.of("Chat") : msg.getAgents());
        input.setModelOptions(msg == null ? ModelOptions.getDefaultForOllama() : msg.getModelOptions());
        input.setMessages(msg == null ? Set.of(message.getPayload()) : msg.getMsg());
        GeneralDomain<Input> domain = new GeneralDomain<>(input, inputDriver.getDomainEventBus());
        DomainEventContext context = new DomainEventContext(domain);
        TopicKey topicKey = Topics.USER_INPUT.copy().append(input.getId());
        inputDriver.publishOn(topicKey, context).subscribe();
    }

    /**
     * from WebSocketSession info to check is authentication.
     *
     * @param session the {@link WebSocketSession} instance.
     * @return true if is authentication.
     */
    public boolean isAuthentication(WebSocketSession session) {
        Map<String, Object> attributes = session.getAttributes();
        HttpHeaders headers = session.getHandshakeHeaders();
        if (headers.containsKey(WebUtil.X_AUTHENTICATION) || attributes.containsKey(WebUtil.X_AUTHENTICATION)) {
            // from header get token
            Supplier<Optional<String>> getHeaderOpt =
                    () -> Optional.ofNullable(headers.getFirst(WebUtil.X_AUTHENTICATION));

            // from attributes get token
            Supplier<Optional<String>> getAttributesOpt =
                    () -> Optional.ofNullable(attributes.get(WebUtil.X_AUTHENTICATION)).map(Object::toString);

            return Optionals.firstNonEmpty(getHeaderOpt, getAttributesOpt)
                    .map(token -> {
                        Jwt jwt;
                        try {
                            jwt = jwtDecoder.decode(token);
                        } catch (JwtException ex) {
                            log.error("websocket authentication error", ex);
                            return false;
                        }
                        Instant expiresAt = jwt.getExpiresAt();
                        // check expires
                        return expiresAt == null || expiresAt.isAfter(DateUtil.now().toInstant());
                    })
                    .orElse(false);
        }
        return false;
    }

    @Override
    public void destroy() throws Exception {
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
