package cc.allio.turbo.modules.ai.agent;

import cc.allio.turbo.common.event.Observable;
import cc.allio.turbo.modules.ai.Input;
import cc.allio.turbo.modules.ai.resources.AIResources;
import cc.allio.turbo.modules.ai.task.Response;
import cc.allio.turbo.modules.ai.task.Task;
import cc.allio.turbo.modules.ai.task.TaskContext;
import cc.allio.turbo.modules.ai.task.action.ActionRegistry;
import org.springframework.ai.chat.model.ChatModel;

import java.util.List;

/**
 * System Manager Assistant (SMA) Agent implementation.
 *
 * @author j.x
 * @since 0.2.0
 */
public class SMAAgent extends BaseResourceAgent {

    public SMAAgent(AIResources resources, ActionRegistry actionRegistry) {
        super(resources, actionRegistry);
    }

    @Override
    public Observable<Response> invoke(ChatModel model, Input input) {
        // create the task
        TaskContext taskContext = new TaskContext();
        Task task = new Task(getLiteralActionNames(), actionRegistry);

        return null;
    }

    /**
     *
     */
    @Override
    public void install() {

    }

    /**
     *
     * @return
     */
    @Override
    public List<String> getLiteralActionNames() {
        return List.of();
    }

    @Override
    public String getName() {
        return "SMA";
    }
}
