package cc.allio.turbo.modules.ai.runtime.tool;

import cc.allio.uno.core.util.JsonUtils;
import cc.allio.uno.core.util.map.OptionalMap;

import java.util.Map;
import java.util.Optional;

/**
 * Model Function Tool
 * <p>
 * refer to <a href="https://platform.openai.com/docs/guides/function-calling#overview">openai function calling.</a>
 *
 * @author j.x
 * @since 0.2.0
 */
public interface FunctionTool extends Tool {

    /**
     * the tool name
     */
    String name();

    /**
     * the tool description
     */
    String description();

    /**
     * the tool parameters (json schema format)
     */
    Map<String, Object> parameters();

    /**
     * of json create {@link FunctionTool}
     *
     * @param json the json map
     * @return
     */
    static FunctionTool of(Map<String, Object> json) {
        OptionalMap<String> optionalMap = OptionalMap.of(json);
        // read
        Optional<String> type = optionalMap.get("type", String.class);
        if (type.isPresent() && type.get().equals(Type.Function.name())) {
            Object obj = optionalMap.get("function").orElse(null);
            if (obj == null) {
                return null;
            }
            return JsonUtils.parse(JsonUtils.toJson(obj), DefaultTool.class);
        }
        return null;
    }

    @Override
    default Type type() {
        return Type.Function;
    }
}
