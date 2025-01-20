package cc.allio.turbo.modules.ai.task;

import cc.allio.turbo.modules.ai.agent.Agent;
import cc.allio.turbo.modules.ai.task.action.Action;
import cc.allio.turbo.modules.ai.task.action.ActionRegistry;
import cc.allio.uno.core.chain.Chain;
import cc.allio.uno.core.chain.DefaultChain;
import com.google.common.collect.Lists;
import reactor.core.publisher.Mono;

import java.util.List;

public class Task {

    private final Chain<TaskContext, Response> planingChain;

    public Task( List<String> literalActionNames, ActionRegistry actionRegistry) {
        List<Action> actions = Lists.newCopyOnWriteArrayList();

        // get action
        for (String actionName : literalActionNames) {
            actionRegistry.getAction(actionName).ifPresent(actions::add);
        }

        this.planingChain = new DefaultChain<>(actions);

    }

    Mono<Response> execute(TaskContext taskContext) {
        return planingChain.proceed(() -> taskContext);
    }
}
