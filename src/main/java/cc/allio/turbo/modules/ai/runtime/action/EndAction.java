package cc.allio.turbo.modules.ai.runtime.action;

import cc.allio.turbo.modules.ai.Output;
import cc.allio.turbo.modules.ai.runtime.Environment;
import cc.allio.uno.core.chain.Chain;
import cc.allio.uno.core.chain.ChainContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Priority;
import java.util.Optional;

/**
 * end of task
 *
 * @author j.x
 * @since 0.2.0
 */
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
