package cc.allio.turbo.modules.ai.agent;

import cc.allio.turbo.common.domain.*;
import cc.allio.turbo.modules.ai.chat.exception.ChatException;
import cc.allio.turbo.modules.ai.driver.Driver;
import cc.allio.turbo.modules.ai.driver.Topics;
import cc.allio.turbo.modules.ai.driver.model.Input;
import cc.allio.turbo.modules.ai.driver.model.Order;
import cc.allio.turbo.modules.ai.driver.model.Output;
import cc.allio.turbo.modules.ai.entity.AIMessage;
import cc.allio.turbo.modules.ai.enums.MessageStatus;
import cc.allio.turbo.modules.ai.exception.AgentInitializationException;
import cc.allio.turbo.modules.ai.chat.resources.AIResources;
import cc.allio.turbo.modules.ai.exception.DispatchException;
import cc.allio.turbo.modules.ai.store.ChatMessageStore;
import cc.allio.turbo.modules.ai.store.InMemoryChatMessageStore;
import cc.allio.uno.core.bus.Pathway;
import cc.allio.uno.core.bus.Topic;
import cc.allio.uno.core.bus.TopicKey;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.function.UnaryOperator;

/**
 * control several agents.
 *
 * @author j.x
 * @since 0.2.0
 */
@Slf4j
public class Supervisor implements InitializingBean {

    private final Driver<Input> inputDriver;
    private final Driver<Output> outputDriver;
    private final AgentRegistry agentRegistry;
    private final AIResources resources;
    private final ChatMessageStore chatMessageStore;

    public Supervisor(Driver<Input> inputDriver,
                      Driver<Output> outputDriver,
                      AgentRegistry agentRegistry,
                      AIResources resources,
                      ObjectProvider<ChatMessageStore> chatMessageStorageObjectProvider) {
        this.inputDriver = inputDriver;
        this.outputDriver = outputDriver;
        this.agentRegistry = agentRegistry;
        this.resources = resources;
        this.chatMessageStore = chatMessageStorageObjectProvider.getIfAvailable(InMemoryChatMessageStore::new);
    }

    /**
     * initialization agent system
     */
    void setup() throws AgentInitializationException {
        // load resource
        resources.readNow();
        for (Agent agent : agentRegistry.getAll()) {
            if (agent instanceof ResourceAgent resourceAgent) {
                resourceAgent.install(resources, chatMessageStore);
            }
        }
    }

    /**
     * @see #doSupervise(TopicKey, UnaryOperator)
     */
    public Disposable doSupervise(TopicKey userChatInputTopic) {
        return doSupervise(userChatInputTopic, ThreadLocalWebDomainEventContext::new);
    }

    /**
     * from user input start topic build and supervise agent.
     *
     * @param userChatInputTopic user chat input topic.
     * @param refineEventContext according environment define chat in authentication web or otherwise environment execution.
     * @return {@link Disposable}
     */
    public Disposable doSupervise(TopicKey userChatInputTopic, UnaryOperator<DomainEventContext> refineEventContext) {
        return inputDriver.subscribeOn(userChatInputTopic)
                .observeMany(refineEventContext)
                .subscribeOn(Schedulers.fromExecutor(Executors.newVirtualThreadPerTaskExecutor()))
                // handle user input
                .flatMap(subscription ->
                        this.dispatch(subscription, refineEventContext).onErrorResume(DispatchException.class, this::handleDispatchError))
                // receive upstream output and publish to evaluation and output
                .flatMap(this::transform)
                .onErrorContinue((err, obj) -> log.error("failed handle message", err))
                .subscribe();
    }

    /**
     * dispatch the user input.
     *
     * @param subscription the subscription instance.
     */
    Flux<Subscription<Output>> dispatch(Subscription<Input> subscription,
                                        UnaryOperator<DomainEventContext> refineEventContext) {
        Input input = subscription.getDomain().orElse(null);
        if (input == null) {
            return Flux.empty();
        }
        String agentName = input.getAgent();
        Agent agent = agentRegistry.get(agentName);
        if (agent != null) {
            return agent.call(Mono.just(input))
                    .observeMany(refineEventContext)
                    .onErrorMap(ChatException.class, err -> new DispatchException(subscription, err));
        }
        return Flux.error(new DispatchException(subscription, "not found agent: " + agentName));
    }

    /**
     * transform the subscription to observable.
     *
     * @param subscription the subscription instance.
     */
    Flux<Topic<DomainEventContext>> transform(Subscription<Output> subscription) {
        Optional<Output> domainOptional = subscription.getDomain();
        if (domainOptional.isEmpty()) {
            return Flux.empty();
        }
        Output output = domainOptional.get();

        GeneralDomain<Output> domain = new GeneralDomain<>(output, outputDriver.getDomainEventBus());
        // publish to evaluation and output.
        DomainEventContext eventContext = new DomainEventContext(domain);

        String conversationId = output.getConversationId();
        String sessionId = output.getSessionId();

        TopicKey evalTopic =
                Topics.EVALUATION.append(conversationId)
                        .append(builder -> builder.text(sessionId).pathway(Pathway.EMPTY));

        TopicKey outputTopic =
                Topics.USER_CHAT_OUTPUT.append(conversationId)
                        .append(builder -> builder.text(sessionId).pathway(Pathway.EMPTY));

        return outputDriver.publishOn(List.of(evalTopic, outputTopic), eventContext);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setup();
    }

    /**
     * when {@link DispatchException} occurred, handle the error.
     * <p>
     * save {@link Input} to storage {@link ChatMessageStore} .
     * if correctly save. then return {@link Output}. (have {@link Input#getInstructions()} will be return same quality {@link Output})
     *
     * @param error {@link DispatchException} error
     * @return
     */
    Flux<Subscription<Output>> handleDispatchError(DispatchException error) {
        Subscription<Input> subscription = error.getSubscription();
        String errorMessage = error.getMessage();
        return Flux.from(Mono.justOrEmpty(subscription.getDomain()))
                .flatMap(input -> {
                    // handel to save messages
                    Set<Order> instructions = input.getInstructions();
                    Long conversationId = Long.valueOf(input.getConversationId());
                    List<AIMessage> messages =
                            instructions.stream()
                                    .map(order -> {
                                        AIMessage message = new AIMessage();
                                        // when order have id, then set id.
                                        // and describe message existing db already.
                                        if (order.getId() != null) {
                                            message.setId(order.getId());
                                        }
                                        message.setSessionId(input.getSessionId());
                                        message.setChatId(conversationId);
                                        message.setState(MessageStatus.ERROR);
                                        message.setErrorMsg(errorMessage);
                                        message.setContent(order.getMessage());
                                        message.setRole(order.getRole());
                                        return message;
                                    })
                                    .toList();

                    chatMessageStore.saveOrUpdateBatch(messages);

                    return Flux.fromIterable(messages)
                            .map(message -> {
                                // covert output
                                Output output = Output.fromAIMessage(message);
                                output.setCreateAt(new Date().getTime());
                                output.setConversationId(input.getConversationId());
                                output.setSessionId(input.getSessionId());
                                output.setErrorMsg(errorMessage);
                                output.setAgent(input.getAgent());
                                output.setExecutionMode(input.getExecutionMode());
                                output.setStatus(message.getState());
                                output.setInput(input);
                                output.setMetadata(Maps.newHashMap());
                                return Subscription.of(output);
                            });
                });
    }

}
