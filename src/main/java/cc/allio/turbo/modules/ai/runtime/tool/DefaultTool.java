package cc.allio.turbo.modules.ai.runtime.tool;

import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class DefaultTool implements FunctionTool {

    private final String name;
    private final String description;
    private final Map<String, Object> parameters;

    @Override
    public String name() {
        return name;
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public Map<String, Object> parameters() {
        return parameters;
    }
}
