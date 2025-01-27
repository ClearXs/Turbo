package cc.allio.turbo.modules.ai.agent;

import cc.allio.turbo.modules.ai.Driver;
import cc.allio.turbo.modules.ai.exception.AgentInitializationException;
import cc.allio.turbo.modules.ai.resources.AIResources;
import cc.allio.turbo.modules.ai.runtime.action.Action;
import cc.allio.turbo.modules.ai.runtime.action.ActionRegistry;
import cc.allio.turbo.modules.ai.runtime.tool.FunctionTool;
import cc.allio.turbo.modules.ai.runtime.tool.Tool;
import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * from {@link AIResources} load agent requirement {@link Action} and {@link Tool}
 *
 * @author j.x
 * @since 0.2.0
 */
public abstract class ResourceAgent implements Agent {

    @Getter
    protected List<String> dispatchActionNames = Lists.newArrayList();
    @Getter
    protected List<Tool> tools = Lists.newArrayList();

    // read agent resources prompt template
    @Getter
    protected String agentPromptTemplate;

    protected final AIResources resources;
    protected final ActionRegistry actionRegistry;
    protected final Driver driver;

    private String name;
    private String description;

    protected ResourceAgent(AIResources resources,
                            ActionRegistry actionRegistry,
                            Driver driver) {
        this.resources = resources;
        this.actionRegistry = actionRegistry;
        this.driver = driver;
    }

    @Override
    public void install() throws AgentInitializationException {
        AIResources.LiteralAgent agent = resources.getAgent(name()).orElse(null);
        if (agent == null) {
            throw new AgentInitializationException("Agent not found");
        }
        this.agentPromptTemplate = agent.getPrompt();
        this.dispatchActionNames = agent.getActions();
        List<Map<String, Object>> toolListMap = agent.getTools();

        List<FunctionTool> tools =
                toolListMap.stream()
                        .map(FunctionTool::of)
                        .toList();
        this.tools.addAll(tools);

        this.name = agent.getName();
        this.description = agent.getDescription();

        // load implementation setup method.
        setup();
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String description() {
        return description;
    }

    /**
     * initialization for the agent
     */
    protected abstract void setup() throws AgentInitializationException;

}
