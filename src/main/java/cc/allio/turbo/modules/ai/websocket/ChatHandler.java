package cc.allio.turbo.modules.ai.websocket;

import cc.allio.turbo.common.domain.DomainEventContext;
import cc.allio.turbo.common.domain.GeneralDomain;
import cc.allio.turbo.common.util.AuthUtil;
import cc.allio.turbo.modules.ai.agent.Agent;
import cc.allio.turbo.modules.ai.agent.Supervisor;
import cc.allio.turbo.modules.ai.api.entity.AIChatSession;
import cc.allio.turbo.modules.ai.driver.Driver;
import cc.allio.turbo.modules.ai.driver.Topics;
import cc.allio.turbo.modules.ai.driver.model.Input;
import cc.allio.turbo.modules.ai.driver.model.Options;
import cc.allio.turbo.modules.ai.driver.model.Order;
import cc.allio.turbo.modules.ai.driver.model.Output;
import cc.allio.turbo.modules.ai.model.ModelOptions;
import cc.allio.turbo.modules.ai.agent.runtime.Variable;
import cc.allio.turbo.modules.auth.domain.AuthThreadLocalWebDomainEventContext;
import cc.allio.turbo.modules.auth.web.websocket.AuthenticationWebSocketHandler;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.bus.Pathway;
import cc.allio.uno.core.bus.TopicKey;
import cc.allio.uno.core.exception.Trys;
import cc.allio.uno.core.util.JsonUtils;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.data.orm.executor.AggregateCommandExecutor;
import cc.allio.uno.data.orm.executor.CommandExecutorFactory;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.data.util.Optionals;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import reactor.core.publisher.Mono;
import reactor.core.Disposable;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * websocket for chat handler.
 *
 * @author j.x
 * @since 0.2.0
 */
@Slf4j
public class ChatHandler extends AuthenticationWebSocketHandler implements DisposableBean {

    private final Supervisor supervisor;
    private final Driver<Input> inputDriver;
    private final Driver<Output> outputDriver;
    private final Map<String, CombineDisposable> sessionDisposable;

    public static final String CONVERSATION_ID_NAME = "conversation-id";

    public ChatHandler(Supervisor supervisor, Driver<Input> inputDriver, Driver<Output> outputDriver) {
        this.supervisor = supervisor;
        this.inputDriver = inputDriver;
        this.outputDriver = outputDriver;
        this.sessionDisposable = Maps.newConcurrentMap();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // ensure authentication in context
        super.afterConnectionEstablished(session);

        String conversationId = getConversationId(session);

        if (StringUtils.isBlank(conversationId)) {
            session.close(CloseStatus.NOT_ACCEPTABLE);
            return;
        }

        String sessionId = session.getId();

        AggregateCommandExecutor executor = CommandExecutorFactory.getDSLExecutor();
        if (executor != null) {

            boolean exist = executor.existTable(AIChatSession.class);
            if (!exist) {
                executor.createTable(AIChatSession.class);
            }

            AIChatSession chatSession = new AIChatSession();
            chatSession.setId(sessionId);
            chatSession.setChatId(Long.valueOf(conversationId));
            String userId = AuthUtil.getUserId();
            chatSession.setUserId(Trys.onContinue(() -> Long.valueOf(userId)));
            executor.saveOrUpdate(chatSession);
        }

        Authentication principal = (Authentication) session.getPrincipal();
        UnaryOperator<DomainEventContext> refineFunc = context -> new AuthThreadLocalWebDomainEventContext(context, principal);

        // agent handle user input

        TopicKey inputTopic = Topics.USER_CHAT_INPUT.append(conversationId).append(TopicKey.of(sessionId, Pathway.EMPTY));
        Disposable inputDisposable = supervisor.doSupervise(inputTopic, refineFunc);

        // subscribe output
        TopicKey outputTopic = Topics.USER_CHAT_OUTPUT.append(conversationId).append(TopicKey.of(sessionId, Pathway.EMPTY));
        var outputDisposable =
                outputDriver.subscribeOn(outputTopic)
                        .observeMany(refineFunc)
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

        sessionDisposable.put(sessionId, new CombineDisposable(inputDisposable, outputDisposable));
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

        if (log.isDebugEnabled()) {
            log.debug("receive message: {}", message.getPayload());
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
        input.setModelOptions(msg == null ? ModelOptions.getDefaultForLlama() : msg.getModelOptions());
        input.setInstructions(msg == null ? Sets.newHashSet(Order.toUser((message.getPayload()))) : msg.getInstructions());
        input.setOptions(msg == null ? new Options() : msg.getOptions());


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

        Supplier<Optional<String>> getUrlOpt =
                () -> Optional.ofNullable(session.getUri())
                        .map(URI::getRawPath)
                        .map(path -> path.substring(path.lastIndexOf(StringPool.SLASH) + 1));

        return Optionals.firstNonEmpty(getUrlOpt, getHeaderOpt, getAttributesOpt).orElse(StringPool.EMPTY);
    }

    @Override
    public void destroy() throws Exception {
        if (!sessionDisposable.isEmpty()) {
            Collection<CombineDisposable> disposables = sessionDisposable.values();

            for (Disposable disposable : disposables) {
                disposable.dispose();
            }
        }
        // clear
        sessionDisposable.clear();
    }

    static class CombineDisposable implements Disposable {
        private final Collection<Disposable> disposables;

        public CombineDisposable(Disposable... disposables) {
            this(Lists.newArrayList(disposables));
        }

        public CombineDisposable(Collection<Disposable> disposables) {
            this.disposables = disposables;
        }

        @Override
        public void dispose() {
            for (Disposable disposable : disposables) {
                disposable.dispose();
            }
        }

        @Override
        public boolean isDisposed() {
            return disposables.stream().allMatch(Disposable::isDisposed);
        }
    }
}
