package cc.allio.turbo.modules.ai.websocket;

import cc.allio.turbo.common.domain.DomainEventContext;
import cc.allio.turbo.common.domain.GeneralDomain;
import cc.allio.turbo.common.util.AuthUtil;
import cc.allio.turbo.common.util.WebUtil;
import cc.allio.turbo.modules.ai.agent.Agent;
import cc.allio.turbo.modules.ai.api.entity.AIChatSession;
import cc.allio.turbo.modules.ai.driver.Driver;
import cc.allio.turbo.modules.ai.driver.Topics;
import cc.allio.turbo.modules.ai.driver.model.Input;
import cc.allio.turbo.modules.ai.driver.model.Output;
import cc.allio.turbo.modules.ai.model.ModelOptions;
import cc.allio.turbo.modules.ai.agent.runtime.Variable;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.bus.Pathway;
import cc.allio.uno.core.bus.TopicKey;
import cc.allio.uno.core.util.DateUtil;
import cc.allio.uno.core.util.JsonUtils;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.data.orm.executor.AggregateCommandExecutor;
import cc.allio.uno.data.orm.executor.CommandExecutorFactory;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.data.util.Optionals;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import reactor.core.publisher.Mono;
import reactor.core.Disposable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collection;
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
public class ChatHandler extends TextWebSocketHandler implements DisposableBean {

    private final Driver<Input> inputDriver;
    private final Driver<Output> outputDriver;

    private final Map<String, Disposable> sessionDisposable;

    public static final String CONVERSATION_ID_NAME = "conversation-id";

    public ChatHandler(Driver<Input> inputDriver, Driver<Output> outputDriver) {
        this.inputDriver = inputDriver;
        this.outputDriver = outputDriver;
        this.sessionDisposable = Maps.newConcurrentMap();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        AggregateCommandExecutor executor = CommandExecutorFactory.getDSLExecutor();
        String conversationId = getConversationId(session);

        if (StringUtils.isBlank(conversationId)) {
            session.close(CloseStatus.NOT_ACCEPTABLE);
            return;
        }

        String sessionId = session.getId();

        if (executor != null) {
            AIChatSession chatSession = new AIChatSession();
            chatSession.setId(sessionId);
            chatSession.setChatId(Long.valueOf(conversationId));
            Long userId = AuthUtil.getUserId();
            chatSession.setUserId(userId);
            executor.saveOrUpdate(chatSession);
        }

        TopicKey outputTopic = Topics.USER_CHAT_OUTPUT.append(conversationId).append(TopicKey.of(sessionId, Pathway.EMPTY));

        var disposable = outputDriver.subscribeOn(outputTopic)
                .observeMany()
                .flatMap(subscription -> Mono.justOrEmpty(subscription.getDomain()))
                .flatMap(output -> {
                    Input input = output.getInput();
                    return Mono.justOrEmpty(input)
                            .flatMap(i -> {
                                if (i instanceof WsInput wsInput) {
                                    return Mono.just(wsInput.getSession());
                                }
                                return Mono.empty();
                            })
                            .doOnNext(webSocketSession -> {
                                String message = JsonUtils.toJson(output);
                                TextMessage textMessage = new TextMessage(message.getBytes(StandardCharsets.UTF_8));
                                try {
                                    session.sendMessage(textMessage);
                                } catch (IOException ex) {
                                    log.error("failed ws send msg: {}", message, ex);
                                }
                            });
                })
                .subscribe();

        sessionDisposable.put(sessionId, disposable);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String sessionId = session.getId();

        if (sessionDisposable.containsKey(sessionId)) {
            Disposable disposable = sessionDisposable.get(sessionId);

            if (disposable != null) {
                disposable.dispose();
            }

            sessionDisposable.remove(sessionId);
        }
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

        String conversationId = getConversationId(session);
        input.setConversationId(conversationId);

        String sessionId = session.getId();
        input.setSession(session);
        input.setSessionId(sessionId);

        input.setVariable(msg == null ? new Variable() : msg.getVariable());
        input.setAgent(msg == null ? Agent.CHAT_AGENT : msg.getAgent());
        input.setModelOptions(msg == null ? ModelOptions.getDefaultForOllama() : msg.getModelOptions());
        input.setMessages(msg == null ? Sets.newHashSet(message.getPayload()) : msg.getMsgs());
        GeneralDomain<Input> domain = new GeneralDomain<>(input, inputDriver.getDomainEventBus());
        DomainEventContext context = new DomainEventContext(domain);

        TopicKey inputTopic = Topics.USER_CHAT_INPUT.append(conversationId).append(TopicKey.of(sessionId, Pathway.EMPTY));

        inputDriver.publishOn(inputTopic, context).subscribe();
    }

    String getConversationId(WebSocketSession session) {
        Map<String, Object> attributes = session.getAttributes();
        HttpHeaders headers = session.getHandshakeHeaders();

        // from header get token
        Supplier<Optional<String>> getHeaderOpt =
                () -> Optional.ofNullable(headers.getFirst(CONVERSATION_ID_NAME));

        // from attributes get token
        Supplier<Optional<String>> getAttributesOpt =
                () -> Optional.ofNullable(attributes.get(CONVERSATION_ID_NAME)).map(Object::toString);

        return Optionals.firstNonEmpty(getHeaderOpt, getAttributesOpt).orElse(StringPool.EMPTY);
    }

    @Override
    public void destroy() throws Exception {
        if (!sessionDisposable.isEmpty()) {
            Collection<Disposable> disposables = sessionDisposable.values();

            for (Disposable disposable : disposables) {
                disposable.dispose();
            }
        }
        // clear
        sessionDisposable.clear();
    }
}
