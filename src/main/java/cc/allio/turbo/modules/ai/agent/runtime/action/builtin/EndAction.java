package cc.allio.turbo.modules.ai.agent.runtime.action.builtin;

import cc.allio.turbo.modules.ai.agent.runtime.Environment;
import cc.allio.turbo.modules.ai.agent.runtime.action.Action;
import cc.allio.turbo.modules.ai.agent.runtime.action.ActionContext;
import cc.allio.turbo.modules.ai.driver.model.Output;
import cc.allio.uno.core.chain.Chain;
import cc.allio.uno.core.chain.ChainContext;
import jakarta.annotation.Priority;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Priority(Integer.MAX_VALUE)
public class EndAction implements Action {

    @Override
    public Flux<Output> executeMany(Chain<Environment, Output> chain, ChainContext<Environment> context) throws Throwable {
        if (context instanceof ActionContext actionContext) {
            Optional<Output> output = actionContext.takeOutput();

            return Flux.from(Mono.justOrEmpty(output));
        }
        return Flux.empty();
    }

    @Override
    public String getName() {
        return END;
    }
}
