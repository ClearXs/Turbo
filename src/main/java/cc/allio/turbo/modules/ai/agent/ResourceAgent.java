package cc.allio.turbo.modules.ai.agent;

import cc.allio.turbo.modules.ai.exception.AgentInitializationException;
import cc.allio.turbo.modules.ai.resources.AIResources;
import cc.allio.turbo.modules.ai.runtime.action.Action;
import cc.allio.turbo.modules.ai.runtime.action.ActionRegistry;
import cc.allio.turbo.modules.ai.runtime.tool.FunctionTool;
import cc.allio.turbo.modules.ai.runtime.tool.Tool;
import cc.allio.turbo.modules.ai.runtime.tool.ToolRegistry;
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

    protected final ActionRegistry actionRegistry;
    protected final ToolRegistry toolRegistry;

    private String description;

    protected ResourceAgent(ToolRegistry toolRegistry, ActionRegistry actionRegistry) {
        this.actionRegistry = actionRegistry;
        this.toolRegistry = toolRegistry;
    }

    @Override
    public void install(AIResources.LiteralAgent agent) throws AgentInitializationException {
        if (agent == null) {
            throw new AgentInitializationException("Agent not found");
        }

        if (!agent.getName().equals(name())) {
            throw new AgentInitializationException("Agent name not match");
        }

        this.agentPromptTemplate = agent.getPrompt();
        this.dispatchActionNames = agent.getActions();
        List<Map<String, Object>> toolListMap = agent.getTools();

        List<FunctionTool> fileTools = toolListMap.stream().map(FunctionTool::of).toList();
        this.tools.addAll(fileTools);

        // load external-tools
        List<String> externalTools = agent.getExternalTools();
        for (String externalTool : externalTools) {
            FunctionTool functionTool = toolRegistry.get(externalTool);
            if (functionTool != null) {
                this.tools.add(functionTool);
            }
        }

        this.description = agent.getDescription();

        // load implementation setup method.
        setup();
    }

    @Override
    public String description() {
        return description;
    }

    /**
     * initialization for the agent
     */
    protected void setup() throws AgentInitializationException {

    }

}
