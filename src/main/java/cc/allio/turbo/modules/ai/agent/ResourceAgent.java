package cc.allio.turbo.modules.ai.agent;

import cc.allio.turbo.modules.ai.exception.AgentInitializationException;
import cc.allio.turbo.modules.ai.resources.AIResources;
import cc.allio.turbo.modules.ai.runtime.action.Action;
import cc.allio.turbo.modules.ai.runtime.action.ActionRegistry;
import cc.allio.turbo.modules.ai.runtime.tool.FunctionTool;
import cc.allio.turbo.modules.ai.runtime.tool.Tool;
import cc.allio.turbo.modules.ai.runtime.tool.ToolRegistry;
import cc.allio.uno.core.util.CollectionUtils;
import com.google.common.collect.Sets;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * from {@link AIResources} load agent requirement {@link Action} and {@link Tool}
 *
 * @author j.x
 * @since 0.2.0
 */
public abstract class ResourceAgent implements Agent {

    @Getter
    protected Set<String> dispatchActionNames = Sets.newHashSet();
    @Getter
    protected Set<FunctionTool> tools = Sets.newHashSet();

    // read agent resources prompt template
    @Getter
    protected String promptTemplate;

    protected final ActionRegistry actionRegistry;
    protected final ToolRegistry toolRegistry;

    private String description;

    protected ResourceAgent(ToolRegistry toolRegistry, ActionRegistry actionRegistry) {
        this.actionRegistry = actionRegistry;
        this.toolRegistry = toolRegistry;
    }

    public void install(AIResources  resources) throws AgentInitializationException {
        if (resources == null) {
            throw new AgentInitializationException("Resources not found");
        }

        AIResources.LiteralAgent literalAgent = resources.detectOfAgent(name()).orElse(null);

        if (literalAgent == null) {
            throw new AgentInitializationException("Agent not found");
        }

        // set prompt template
        this.promptTemplate = literalAgent.getPrompt();

        // set actions
        List<String> actions = literalAgent.getActions();
        if (CollectionUtils.isNotEmpty(actions)) {
            this.dispatchActionNames = Sets.newHashSet(actions);
        }

        // set tools
        List<Map<String, Object>> toolListMap = literalAgent.getTools();
        if (CollectionUtils.isEmpty(toolListMap)) {
            List<FunctionTool> fileTools = toolListMap.stream().map(FunctionTool::of).toList();
            this.tools.addAll(fileTools);
        }


        // load external-tools
        List<String> externalTools = literalAgent.getExternalTools();

        if (CollectionUtils.isNotEmpty(externalTools)) {
            for (String externalTool : externalTools) {
                FunctionTool functionTool = toolRegistry.get(externalTool);
                if (functionTool != null) {
                    this.tools.add(functionTool);
                }
            }
        }

        // set description
        this.description = literalAgent.getDescription();

        // load implementation setup method.
        setup();
    }

    @Override
    public void addTemporalTool(FunctionTool tool) {
        if (this.tools == null) {
            this.tools = Sets.newHashSet();
        }
        this.tools.add(tool);
    }

    @Override
    public void addDispatchActionName(String actionName) {
        if (this.dispatchActionNames == null) {
            this.dispatchActionNames = Sets.newHashSet();
        }
        this.dispatchActionNames.add(actionName);
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
