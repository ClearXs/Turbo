package cc.allio.turbo.modules.ai.agent;

import cc.allio.turbo.modules.ai.registry.CompositeComponentRegistry;
import cc.allio.turbo.modules.ai.registry.Registry;

/**
 * {@link Agent} of {@link Registry}
 *
 * @author j.x
 * @since 0.2.0
 */
public class AgentRegistry extends CompositeComponentRegistry<Agent, String> {

    public AgentRegistry() {
        super(Agent::name);
    }
}
