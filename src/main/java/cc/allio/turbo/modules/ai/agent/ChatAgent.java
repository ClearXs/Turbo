package cc.allio.turbo.modules.ai.agent;

import cc.allio.turbo.common.domain.Observable;
import cc.allio.turbo.modules.ai.Driver;
import cc.allio.turbo.modules.ai.Input;
import cc.allio.turbo.modules.ai.Output;
import cc.allio.turbo.modules.ai.exception.AgentInitializationException;
import cc.allio.turbo.modules.ai.resources.AIResources;
import cc.allio.turbo.modules.ai.runtime.Task;
import cc.allio.turbo.modules.ai.runtime.action.ActionRegistry;
import reactor.core.publisher.Mono;

public class ChatAgent extends ResourceAgent {

    public ChatAgent(AIResources resources, ActionRegistry actionRegistry, Driver driver) {
        super(resources, actionRegistry, driver);
    }


    @Override
    protected void setup() throws AgentInitializationException {

    }

    @Override
    public Observable<Output> call(Mono<Input> input) {
        return new Task(this, actionRegistry).execute(input);
    }
}
