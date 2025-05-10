package cc.allio.turbo.modules.ai.agent.runtime;

import cc.allio.turbo.common.db.entity.MapEntity;
import cc.allio.turbo.modules.ai.driver.model.Input;
import cc.allio.turbo.modules.ai.agent.Agent;
import cc.allio.turbo.modules.ai.entity.AIMessage;
import cc.allio.turbo.modules.ai.model.AgentModel;
import cc.allio.uno.core.api.Copyable;
import cc.allio.uno.core.api.Self;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Agent runtime environment
 *
 * @author j.x
 * @since 0.2.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Environment extends MapEntity implements Self<Environment>, Copyable<Environment> {

    // default name
    public static final String AGENT_NAME = "AGENT_NAME";
    public static final String AGENT_DESCRIPTION = "AGENT_DESCRIPTION";
    public static final String TASK_ID = "TASK_ID";

    private Input input;
    private AgentModel agentModel;
    private Agent agent;

    private List<AIMessage> userMessage;
    private AIMessage assistantMessage;

    public Environment() {
        // set all os environment
        putAll(System.getenv());
    }

    /**
     * inject {@link Agent} properties for environment.
     *
     * @param agent the {@link Agent} instance.
     */
    public Environment injectOf(Agent agent) {
        put(AGENT_NAME, agent.name());
        put(AGENT_DESCRIPTION, agent.description());
        return self();
    }

    /**
     * inject {@link Variable} properties for environment.
     *
     * @param variable the {@link Variable} instance
     */
    public Environment injectOf(Variable variable) {
        putAll(variable);
        return self();
    }

    /**
     * inject {@link Task} properties for environment.
     *
     * @param task the {@link Task} instance
     */
    public Environment injectOf(Task task) {
        put(TASK_ID, task.getId());
        return self();
    }

    @Override
    public Environment copy() {
        Environment environment = new Environment();
        environment.putAll(this);
        return environment;
    }
}
