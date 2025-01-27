package cc.allio.turbo.modules.ai.runtime.tool;

import cc.allio.turbo.modules.ai.CompositeComponentRegistry;

public class ToolRegistry extends CompositeComponentRegistry<FunctionTool, String> {

    public ToolRegistry() {
        super(FunctionTool::name);
    }
}
