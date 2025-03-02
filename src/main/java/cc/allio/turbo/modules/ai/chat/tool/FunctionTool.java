package cc.allio.turbo.modules.ai.chat.tool;

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

    String TYPE_KEY = "type";
    String NAME_KEY = "name";
    String DESCRIPTION_KEY = "description";
    String PARAMETERS_KEY = "parameters";

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
     * json map create {@link FunctionTool}.
     * <p>
     * typically tool json maps like:
     * <code>
     * {"type":"function","function":{"name":"get_current_weather","description":"Get the current weather in a given location","parameters":{"type":"object","properties":{"location":{"type":"string","description":"The city and state, e.g. San Francisco, CA"},"unit":{"type":"string","enum":["celsius","fahrenheit"]}},"required":["location"]}}}
     * </code>
     *
     * @param json the json map
     * @return the {@link FunctionTool} instance or null if parse failed.
     */
    static FunctionTool of(Map<String, Object> json) {
        OptionalMap<String> optionalMap = OptionalMap.of(json);
        // read
        Optional<String> optionalType = optionalMap.get(TYPE_KEY, String.class);
        if (optionalType.isPresent() && optionalType.get().equals(Type.Function.getName())) {
            Optional<Map<String, Object>> optionalObj = optionalMap.getMap(FUNCTION_KEY, String.class, Object.class);
            if (optionalObj.isEmpty()) {
                return null;
            }
            DefaultTool.DefaultToolBuilder builder = DefaultTool.builder();
            // set of tool instance
            optionalObj.map(OptionalMap::of)
                    .ifPresent(objMap -> {
                        objMap.get(NAME_KEY, String.class).ifPresent(builder::name);
                        objMap.get(DESCRIPTION_KEY, String.class).ifPresent(builder::description);
                        objMap.getMap(PARAMETERS_KEY, String.class, Object.class).ifPresent(builder::parameters);
                    });
            return builder.build();
        }
        return null;
    }

    @Override
    default Type type() {
        return Type.Function;
    }
}
