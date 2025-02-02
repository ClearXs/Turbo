package cc.allio.turbo.modules.ai.runtime;

import cc.allio.turbo.common.domain.Observable;
import cc.allio.turbo.modules.ai.Input;
import cc.allio.turbo.modules.ai.Output;
import cc.allio.turbo.modules.ai.agent.Agent;
import cc.allio.turbo.modules.ai.model.AgentModel;
import cc.allio.turbo.modules.ai.runtime.action.Action;
import cc.allio.turbo.modules.ai.runtime.action.ActionRegistry;
import cc.allio.turbo.modules.ai.runtime.action.EndAction;
import cc.allio.uno.core.chain.Chain;
import cc.allio.uno.core.chain.DefaultChain;
import cc.allio.uno.core.util.id.IdGenerator;
import com.google.common.collect.Lists;
import lombok.Getter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * user task.
 * <p>
 * make plan
 *
 * @author j.x
 * @since 0.2.0
 */
public class Task {

    @Getter
    private final Long id;
    private final Agent agent;
    private final ActionRegistry actionRegistry;

    public Task(Agent agent, ActionRegistry actionRegistry) {
        this.id = IdGenerator.defaultGenerator().getNextId();
        this.agent = agent;
        this.actionRegistry = actionRegistry;
    }

    /**
     * do execute current user task
     *
     * @param taskContext the task context instance
     * @return
     */
    public Observable<Output> execute(Mono<Input> inputMono) {
        Flux<Output> source =
                inputMono.flatMapMany(
                        input -> {
                            Environment environment = new Environment().injectOf(agent).injectOf(this);
                            AgentModel agentModel = new AgentModel(input.getModelOptions());
                            TaskContext taskContext =
                                    TaskContext.builder()
                                            .agentModel(agentModel)
                                            .input(input)
                                            .environment(environment)
                                            .agent(agent)
                                            .task(this)
                                            .build();
                            Chain<TaskContext, Output> planning = buildPlaning();
                            return planning.processMany(() -> taskContext);
                        });
        return Observable.from(source);
    }

    /**
     * specific task planing chain.
     */
    Chain<TaskContext, Output> buildPlaning() {
        List<String> dispatchActionNames = agent.getDispatchActionNames();
        List<Action> actions = Lists.newCopyOnWriteArrayList();
        // get action
        for (String actionName : dispatchActionNames) {
            actionRegistry.getAction(actionName).ifPresent(actions::add);
        }

        if (!dispatchActionNames.contains(Action.END)) {
            actions.addLast(new EndAction());
        }

        return new DefaultChain<>(actions);
    }
}
