package cc.allio.turbo.modules.ai.runtime;

import cc.allio.turbo.common.db.entity.MapEntity;
import cc.allio.turbo.modules.ai.agent.Agent;
import cc.allio.uno.core.api.Self;

/**
 * Agent runtime environment
 *
 * @author j.x
 * @since 0.2.0
 */
public class Environment extends MapEntity implements Self<Environment> {

    // default name
    public static final String AGENT_NAME = "agent.name";
    public static final String AGENT_DESCRIPTION = "agent.description";

    public static final String TASK_ID = "task.id";

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
     * inject {@link Task} properties for environment.
     *
     * @param task the {@link Task} instance
     */
    public Environment injectOf(Task task) {
        put(TASK_ID, task.getId());
        return self();
    }
}
