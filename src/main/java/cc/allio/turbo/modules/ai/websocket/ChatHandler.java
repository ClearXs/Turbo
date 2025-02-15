package cc.allio.turbo.modules.ai.websocket;

import cc.allio.turbo.common.domain.DomainEventContext;
import cc.allio.turbo.common.domain.GeneralDomain;
import cc.allio.turbo.common.domain.Subscription;
import cc.allio.turbo.common.util.WebUtil;
import cc.allio.turbo.modules.ai.Driver;
import cc.allio.turbo.modules.ai.Input;
import cc.allio.turbo.modules.ai.Output;
import cc.allio.turbo.modules.ai.Topics;
import cc.allio.uno.core.bus.TopicKey;
import cc.allio.uno.core.util.DateUtil;
import cc.allio.uno.core.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.util.Optionals;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.reactive.socket.*;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * websocket for chat handler.
 *
 * @author j.x
 * @since 0.2.0
 */
@Slf4j
public class ChatHandler implements WebSocketHandler, InitializingBean, DisposableBean {

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
                        .flatMap(subscription -> Flux.from(handleOutput(subscription)))
                        .subscribe();
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        // unique id for session.
        HandshakeInfo handshakeInfo = session.getHandshakeInfo();
        if (!isAuthentication(handshakeInfo)) {
            return session.close(CloseStatus.NOT_ACCEPTABLE);
        }
        return session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                // create ws input
                .map(message -> {
                    Message msg = JsonUtils.parse(message, Message.class);
                    WsInput input = new WsInput();
                    String sessionId = session.getId();
                    input.setSession(session);
                    input.setSessionId(sessionId);
                    input.setVariable(msg.getVariable());
                    input.setAgents(msg.getAgents());
                    input.setModelOptions(msg.getModelOptions());
                    input.setMessages(msg.getMsg());
                    return input;
                })
                .flatMap(input -> {
                    GeneralDomain<Input> domain = new GeneralDomain<>(input, inputDriver.getDomainEventBus());
                    DomainEventContext context = new DomainEventContext(domain);
                    TopicKey topicKey = Topics.USER_INPUT.append(input.getSessionId());
                    return inputDriver.publishOn(topicKey, context);
                })
                // simple record to log
                .onErrorContinue((throwable, o) -> log.error("receive message error", throwable))
                .then();
    }

    /**
     * from handshake info to check is authentication.
     *
     * @param handshakeInfo the {@link HandshakeInfo} instance.
     * @return true if is authentication.
     */
    public boolean isAuthentication(HandshakeInfo handshakeInfo) {
        Map<String, Object> attributes = handshakeInfo.getAttributes();
        HttpHeaders headers = handshakeInfo.getHeaders();
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

    Mono<Void> handleOutput(Subscription<Output> subscription) {
        return subscription.getDomain()
                .flatMap(output -> {
                    Input input = output.getInput();
                    return Optional.ofNullable(input)
                            .flatMap(i -> {
                                if (i instanceof WsInput wsInput) {
                                    return Optional.ofNullable(wsInput.getSession());
                                }
                                return Optional.empty();
                            })
                            .map(session -> {
                                WebSocketMessage message = session.textMessage(output.getMessage());
                                return session.send(Mono.just(message));
                            });
                })
                .orElse(Mono.empty());
    }

    @Override
    public void destroy() throws Exception {
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
