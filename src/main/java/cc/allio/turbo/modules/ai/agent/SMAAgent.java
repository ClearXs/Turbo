package cc.allio.turbo.modules.ai.agent;

import cc.allio.turbo.common.domain.Observable;
import cc.allio.turbo.modules.ai.Input;
import cc.allio.turbo.modules.ai.Output;
import cc.allio.turbo.modules.ai.runtime.Task;
import cc.allio.turbo.modules.ai.runtime.action.ActionRegistry;
import cc.allio.turbo.modules.ai.runtime.tool.ToolRegistry;
import reactor.core.publisher.Mono;

/**
 * System Manager Assistant (SMA) Agent implementation.
 *
 * @author j.x
 * @since 0.2.0
 */
public class SMAAgent extends ResourceAgent {

    public SMAAgent(ToolRegistry toolRegistry, ActionRegistry actionRegistry) {
        super(toolRegistry, actionRegistry);
    }

    @Override
    public String name() {
        return "SMA";
    }

    @Override
    public Observable<Output> call(Mono<Input> input) {
        return new Task(this, actionRegistry).execute(input);
    }
}
