package cc.allio.turbo.modules.ai.runtime;

import cc.allio.turbo.common.domain.Observable;
import cc.allio.turbo.modules.ai.Input;
import cc.allio.turbo.modules.ai.Output;
import cc.allio.turbo.modules.ai.agent.Agent;
import cc.allio.turbo.modules.ai.model.AgentModel;
import cc.allio.turbo.modules.ai.runtime.action.Action;
import cc.allio.turbo.modules.ai.runtime.action.ActionContext;
import cc.allio.turbo.modules.ai.runtime.action.ActionRegistry;
import cc.allio.uno.core.chain.Chain;
import cc.allio.uno.core.chain.DefaultChain;
import cc.allio.uno.core.util.id.IdGenerator;
import com.google.common.collect.Lists;
import lombok.Getter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

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
    private final Agent formAgent;
    private final ActionRegistry actionRegistry;
    @Getter
    private final Environment environment;

    public Task(Agent formAgent, ActionRegistry actionRegistry) {
        this.id = IdGenerator.defaultGenerator().getNextId();
        this.formAgent = formAgent;
        this.actionRegistry = actionRegistry;
        this.environment = new Environment().injectOf(formAgent).injectOf(this);
    }

    /**
     * @see #execute(Mono, ExecutionMode)
     */
    public Observable<Output> execute(Mono<Input> inputMono) {
        return execute(inputMono, ExecutionMode.STREAM);
    }

    /**
     * do execute current user task
     *
     * @param inputMono the input
     * @param mode      the execution mode
     * @return the {@link Observable} for {@link Output}
     */
    public Observable<Output> execute(Mono<Input> inputMono, ExecutionMode mode) {
        return Observable.from(
                inputMono.flatMapMany(
                        input -> {
                            Environment newEnvironment = environment.copy();
                            if (input.getVariable() != null) {
                                newEnvironment.injectOf(input.getVariable());
                            }
                            AgentModel agentModel = new AgentModel(input.getModelOptions());
                            TaskContext taskContext =
                                    TaskContext.builder()
                                            .agentModel(agentModel)
                                            .input(input)
                                            .environment(newEnvironment)
                                            .agent(formAgent)
                                            .task(this)
                                            .build();
                            Chain<TaskContext, Output> planning = buildPlaning();
                            ActionContext actionContext = new ActionContext(taskContext);
                            actionContext.setMode(mode);
                            return planning.processMany(actionContext);
                        })
        );
    }

    /**
     * specific task planing chain.
     */
    public Chain<TaskContext, Output> buildPlaning() {
        Set<String> dispatchActionNames = formAgent.getDispatchActionNames();
        List<Action> actions = Lists.newCopyOnWriteArrayList();
        // check action
        Action endAction =
                actionRegistry.getAction(Action.END)
                        .orElseThrow(() -> new NoSuchElementException("No End Action existing."));
        // get action
        for (String actionName : dispatchActionNames) {
            // keep end action always last index.
            if (actionName.equals(Action.END)) {
                continue;
            }
            actionRegistry.getAction(actionName).ifPresent(actions::add);
        }
        // set end action
        actions.addLast(endAction);
        return new DefaultChain<>(actions);
    }
}
