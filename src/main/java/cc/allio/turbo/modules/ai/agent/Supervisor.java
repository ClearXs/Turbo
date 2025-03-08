package cc.allio.turbo.modules.ai.agent;

import cc.allio.turbo.common.domain.*;
import cc.allio.turbo.modules.ai.driver.Driver;
import cc.allio.turbo.modules.ai.driver.Topics;
import cc.allio.turbo.modules.ai.driver.model.Input;
import cc.allio.turbo.modules.ai.driver.model.Output;
import cc.allio.turbo.modules.ai.exception.AgentInitializationException;
import cc.allio.turbo.modules.ai.chat.resources.AIResources;
import cc.allio.turbo.modules.ai.agent.runtime.action.ActionRegistry;
import cc.allio.turbo.modules.ai.chat.tool.ToolRegistry;
import cc.allio.uno.core.bus.Pathway;
import cc.allio.uno.core.bus.Topic;
import cc.allio.uno.core.bus.TopicKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;

/**
 * control several agents.
 *
 * @author j.x
 * @since 0.2.0
 */
@Slf4j
public class Supervisor implements InitializingBean, Disposable {

    final Driver<Input> inputDriver;
    final Driver<Output> outputDriver;
    final AgentRegistry agentRegistry;
    final ActionRegistry actionRegistry;
    final ToolRegistry toolRegistry;
    final AIResources resources;

    Disposable disposable;

    public Supervisor(Driver<Input> inputDriver,
                      Driver<Output> outputDriver,
                      AgentRegistry agentRegistry,
                      ActionRegistry actionRegistry,
                      ToolRegistry toolRegistry,
                      AIResources resources) {
        this.inputDriver = inputDriver;
        this.outputDriver = outputDriver;
        this.agentRegistry = agentRegistry;
        this.resources = resources;
        this.actionRegistry = actionRegistry;
        this.toolRegistry = toolRegistry;
    }

    /**
     * initialization agent system
     */
    void setup() throws AgentInitializationException {
        // load resource
        resources.readNow();

        initiateResourceAgents();

        // subscribe user input
        this.disposable =
                inputDriver.subscribeOn(Topics.USER_CHAT_INPUT_PATTERNS)
                        .observeMany()
                        .subscribeOn(Schedulers.fromExecutor(Executors.newVirtualThreadPerTaskExecutor()))
                        // handle user input
                        .flatMap(this::dispatch)
                        // receive upstream output and publish to evaluation and output
                        .flatMap(this::transform)
                        .subscribe();
    }

    void initiateResourceAgents() throws AgentInitializationException {
        for (Agent agent : agentRegistry.getAll()) {
            if (agent instanceof ResourceAgent resourceAgent) {
                resourceAgent.install(resources);
            }
        }
    }

    /**
     * dispatch the user input.
     *
     * @param subscription the subscription instance.
     */
    Flux<Subscription<Output>> dispatch(Subscription<Input> subscription) {
        Input input = subscription.getDomain().orElse(null);
        if (input == null) {
            return Flux.empty();
        }
        String agentName = input.getAgent();
        Agent agent = agentRegistry.get(agentName);
        if (agent != null) {
            return agent.call(Mono.just(input)).observeMany();
        }
        return Flux.empty();
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

    @Override
    public void dispose() {
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
