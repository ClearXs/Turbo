package cc.allio.turbo.modules.ai.runtime.tool;

import cc.allio.turbo.modules.ai.CompositeComponentRegistry;

import java.util.Map;

/**
 * {@link Tool} registry
 *
 * @author j.x
 * @since 0.2.0
 */
public class ToolRegistry extends CompositeComponentRegistry<FunctionTool, String> {

    public ToolRegistry() {
        super(FunctionTool::name);
    }

    @Override
    protected void scan() {
        super.scan();

        // scan ToolObject for tool collection then put tool registry.
        Map<String, ToolObject> toolObjectMap =
                applicationContext.getBeansOfType(ToolObject.class);

        toolObjectMap.values()
                .stream()
                .flatMap(toolObject -> toolObject.getTools().stream())
                .forEach(tool -> put(tool.name(), tool));
    }
}
