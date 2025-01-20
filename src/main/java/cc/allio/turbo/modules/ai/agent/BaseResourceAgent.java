package cc.allio.turbo.modules.ai.agent;

import cc.allio.turbo.modules.ai.exception.AgentInitializationException;
import cc.allio.turbo.modules.ai.resources.AIResources;
import cc.allio.turbo.modules.ai.resources.prompt.SelfPrompt;
import cc.allio.turbo.modules.ai.task.action.ActionRegistry;
import com.google.common.collect.Lists;

import java.util.List;

public abstract class BaseResourceAgent implements Agent {

    protected SelfPrompt prompt;
    protected List<String> literalActionNames = Lists.newArrayList();

    protected AIResources resources;
    protected ActionRegistry actionRegistry;

    protected BaseResourceAgent(AIResources resources, ActionRegistry actionRegistry) {
        this.resources = resources;
        this.actionRegistry = actionRegistry;
    }

    /**
     * initialization for the agent
     */
    protected void setup() throws AgentInitializationException {
        AIResources.LiteralAgent agent = resources.getAgent(getName()).orElse(null);
        if (agent == null) {
            throw new AgentInitializationException("Agent not found");
        }
        this.prompt = new SelfPrompt(agent.getPrompt());
        this.literalActionNames = agent.getActions();
    }

    @Override
    public List<String> getLiteralActionNames() {
        return literalActionNames;
    }
}
