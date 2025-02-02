package cc.allio.turbo.modules.ai.agent;

import cc.allio.turbo.common.domain.*;
import cc.allio.turbo.modules.ai.*;
import cc.allio.turbo.modules.ai.exception.AgentInitializationException;
import cc.allio.turbo.modules.ai.exception.ResourceParseException;
import cc.allio.turbo.modules.ai.resources.AIResources;
import cc.allio.turbo.modules.ai.runtime.action.ActionRegistry;
import cc.allio.uno.core.bus.EventBus;
import cc.allio.uno.core.bus.Topic;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * control several agents.
 *
 * @author j.x
 * @since 0.2.0
 */
@Slf4j
public class AgentController implements InitializingBean, Disposable {

    final EventBus<DomainEventContext> eventBus;
    final Registry<Agent, String> agentRegistry;

    final ActionRegistry actionRegistry;
    final AIResources resources;
    @Getter
    final Driver driver;

    Disposable disposable;

    public AgentController(EventBus<DomainEventContext> eventBus) {
        this.eventBus = eventBus;
        this.agentRegistry = new AgentRegistry();
        this.resources = new AIResources();
        this.actionRegistry = new ActionRegistry(resources);
        this.driver = new Driver(eventBus);
    }

    /**
     * initialization agent system
     */
    void setup() throws ResourceParseException, AgentInitializationException {
        // load resource
        resources.readNow();

        // load all agent
        initiateAgents();

        // subscribe user input
        this.disposable =
                driver.subscribeOn(Topics.USER_INPUT_PATTERNS)
                        .observe()
                        .subscribeOn(Schedulers.fromExecutor(Executors.newVirtualThreadPerTaskExecutor()))
                        // handle user input
                        .flatMapMany(this::dispatch)
                        // receive upstream output and publish to evaluation and output
                        .flatMap(this::transform)
                        .subscribe();
    }

    void initiateAgents() throws AgentInitializationException {
        // load internal agent
        SMAAgent smaAgent = new SMAAgent(resources, actionRegistry, driver);
        smaAgent.install();
        agentRegistry.put(smaAgent.name(), smaAgent);

        ChatAgent chatAgent = new ChatAgent(resources, actionRegistry, driver);
        chatAgent.install();
        agentRegistry.put(smaAgent.name(), chatAgent);
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

        String message = input.getMessage();
        List<String> agents = input.getAgents();
        if (log.isInfoEnabled()) {
            log.info("subscribe user input message {}, use by agents is {}", message, agents);
        }

        MultiObservable<Output> observable = new MultiObservable<>();
        for (String agentName : agents) {
            Agent agent = agentRegistry.get(agentName);
            if (agent != null) {
                observable.concat(agent.call(Mono.just(input)));
            }
        }
        return observable.observeMany();
    }

    /**
     * transform the subscription to observable.
     *
     * @param subscription
     */
    Flux<Topic<DomainEventContext>> transform(Subscription<Output> subscription) {
        if (subscription.getDomain().isPresent()) {
            return Flux.empty();
        }
        Output output = subscription.getDomain().get();
        GeneralDomain<Output> domain = new GeneralDomain<>(output, driver.getDomainEventBus());
        Long inputId = output.getInputId();
        // publish to evaluation and output.
        DomainEventContext eventContext = new DomainEventContext(domain);
        return driver.publishOn(Topics.EVALUATION.append(inputId), eventContext)
                .thenMany(driver.publishOn(Topics.OUTPUT.append(inputId), eventContext));
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
