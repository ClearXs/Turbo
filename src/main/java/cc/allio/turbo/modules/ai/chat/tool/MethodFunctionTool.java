package cc.allio.turbo.modules.ai.chat.tool;

import cc.allio.uno.core.util.JsonUtils;
import lombok.Getter;
import org.springframework.ai.tool.definition.DefaultToolDefinition;
import org.springframework.ai.tool.definition.ToolDefinition;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * java {@link Method} to {@link FunctionTool}
 *
 * @author j.x
 * @see ToolObject
 * @since 0.2.0
 */
public interface MethodFunctionTool extends FunctionTool {

    /**
     * get java method
     *
     * @return the {@link Method}
     */
    Method getMethod();

    /**
     * get method target object
     *
     * @return invoker target
     */
    Object getTarget();

    /**
     * create {@link MethodFunctionTool} from {@link Method}
     *
     * @return
     */
    static MethodFunctionTool from(Method method, Object target) {
//        ToolDefinition definition = DefaultToolDefinition.from(method);
//        String name = definition.name();
//        String description = definition.description();
//        String jsonSchema = definition.inputSchema();
//        return new MethodFunctionToolImpl(name, description, JsonUtils.toMap(jsonSchema), method, target);
        return null;
    }

    @Getter
    class MethodFunctionToolImpl extends DefaultTool implements MethodFunctionTool {

        private final Method method;
        private final Object target;

        public MethodFunctionToolImpl(String name,
                                      String description,
                                      Map<String, Object> parameters,
                                      Method method,
                                      Object target) {
            super(name, description, parameters);
            this.method = method;
            this.target = target;
        }
    }
}
