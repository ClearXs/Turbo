package cc.allio.turbo.modules.ai.agent;

import cc.allio.turbo.common.domain.Observable;
import cc.allio.turbo.modules.ai.Input;
import cc.allio.turbo.modules.ai.Output;
import cc.allio.turbo.modules.ai.runtime.ExecutionMode;
import cc.allio.turbo.modules.ai.runtime.Task;
import cc.allio.turbo.modules.ai.runtime.action.ActionRegistry;
import cc.allio.turbo.modules.ai.runtime.tool.ToolRegistry;
import reactor.core.publisher.Mono;

/**
 * Chat-Bot agent implementation.
 *
 * @author j.x
 * @since 0.2.0
 */
public class ChatAgent extends ResourceAgent {

    public ChatAgent(ToolRegistry toolRegistry, ActionRegistry actionRegistry) {
        super(toolRegistry, actionRegistry);
    }

    public String name() {
        return "Chat";
    }

    @Override
    public Observable<Output> call(Mono<Input> input, ExecutionMode mode) {
        return new Task(this, actionRegistry).execute(input, mode);
    }
}
