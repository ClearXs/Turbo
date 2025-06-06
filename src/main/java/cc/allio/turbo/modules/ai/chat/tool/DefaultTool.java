package cc.allio.turbo.modules.ai.chat.tool;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Builder
@AllArgsConstructor
@EqualsAndHashCode(of = "name")
public class DefaultTool implements FunctionTool {

    private String name;
    private String description;
    private Map<String, Object> parameters;

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
