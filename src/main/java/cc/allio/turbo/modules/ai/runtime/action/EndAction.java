package cc.allio.turbo.modules.ai.runtime.action;

import cc.allio.turbo.modules.ai.Output;
import cc.allio.turbo.modules.ai.runtime.TaskContext;
import cc.allio.uno.core.chain.Chain;
import cc.allio.uno.core.chain.ChainContext;
import reactor.core.publisher.Mono;

import javax.annotation.Priority;

/**
 * end of task
 *
 * @author j.x
 * @since 0.2.0
 */
@Priority(Integer.MAX_VALUE)
public class EndAction implements Action {

    @Override
    public Mono<Output> execute(Chain<TaskContext, Output> chain, ChainContext<TaskContext> context) throws Throwable {
        Output output = context.getIN().getOutput();
        return Mono.justOrEmpty(output);
    }

    @Override
    public String message() {
        return "";
    }

    @Override
    public String getName() {
        return END;
    }
}
