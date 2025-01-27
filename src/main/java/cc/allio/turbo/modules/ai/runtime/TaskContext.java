package cc.allio.turbo.modules.ai.runtime;

import cc.allio.turbo.modules.ai.Input;
import cc.allio.turbo.modules.ai.Output;
import cc.allio.turbo.modules.ai.agent.Agent;
import cc.allio.turbo.modules.ai.model.AgentModel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskContext {

    private Task task;
    private Input input;
    private AgentModel agentModel;
    private Agent agent;
    // runtime environment
    private Environment environment;

    // output
    private Output output;
}
