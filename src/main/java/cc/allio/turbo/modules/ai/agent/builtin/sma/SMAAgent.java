package cc.allio.turbo.modules.ai.agent.builtin.sma;

import cc.allio.turbo.common.domain.Observable;
import cc.allio.turbo.modules.ai.agent.Agent;
import cc.allio.turbo.modules.ai.driver.model.Input;
import cc.allio.turbo.modules.ai.driver.model.Output;
import cc.allio.turbo.modules.ai.agent.ResourceAgent;
import cc.allio.turbo.modules.ai.agent.runtime.Task;
import cc.allio.turbo.modules.ai.agent.runtime.action.ActionRegistry;
import cc.allio.turbo.modules.ai.chat.tool.ToolRegistry;
import org.checkerframework.checker.units.qual.A;
import reactor.core.publisher.Mono;

/**
 * System Manager Assistant (SMA) Agent implementation.
 *
 * @author j.x
 * @since 0.2.0
 */
public class SMAAgent extends ResourceAgent implements Agent {

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
