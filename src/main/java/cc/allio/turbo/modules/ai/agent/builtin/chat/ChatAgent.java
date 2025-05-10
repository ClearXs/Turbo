package cc.allio.turbo.modules.ai.agent.builtin.chat;

import cc.allio.turbo.common.domain.Observable;
import cc.allio.turbo.modules.ai.agent.Agent;
import cc.allio.turbo.modules.ai.driver.model.Input;
import cc.allio.turbo.modules.ai.driver.model.Output;
import cc.allio.turbo.modules.ai.agent.ResourceAgent;
import cc.allio.turbo.modules.ai.agent.runtime.Task;
import cc.allio.turbo.modules.ai.agent.runtime.action.ActionRegistry;
import cc.allio.turbo.modules.ai.chat.tool.ToolRegistry;
import reactor.core.publisher.Mono;

/**
 * Chat-Bot agent implementation.
 *
 * @author j.x
 * @since 0.2.0
 */
public class ChatAgent extends ResourceAgent implements Agent {

    public ChatAgent(ToolRegistry toolRegistry, ActionRegistry actionRegistry) {
        super(toolRegistry, actionRegistry);
    }

    public String name() {
        return "Chat";
    }

    @Override
    public Observable<Output> call(Mono<Input> input) {
        return new Task(this, actionRegistry, chatMessageStore).execute(input);
    }

}
