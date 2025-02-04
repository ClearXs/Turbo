package cc.allio.turbo.modules.ai.runtime.tool;

import cc.allio.uno.core.util.JsonUtils;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * tool object.
 * <p>
 * hava to ways get tools:
 * <ol>
 *     <li>automation scans object to get all tools. base on spring AI support annotation for {@link org.springframework.ai.tool.annotation.Tool}</li>
 *     <li>otherwise allow self register tools.</li>
 * </ol>
 *
 * @author j.x
 * @since 0.2.0
 */
@Slf4j
public abstract class ToolObject implements InitializingBean {

    private final Map<Method, FunctionTool> tools = Maps.newConcurrentMap();
    AtomicBoolean hasScan = new AtomicBoolean(false);

    @Override
    public void afterPropertiesSet() throws Exception {
        setup();
    }

    protected void setup() {
        scan();
    }

    /**
     * @see #registerTool(Method)
     */
    protected void registerTool(String methodName, Class<?>... parameterTypes) {
        try {
            Method method = getClass().getMethod(methodName, parameterTypes);
            registerTool(method);
        } catch (NoSuchMethodException ex) {
            log.error("Failed to register tools for method: {}", methodName, ex);
        }
    }

    /**
     * register tool according to {@link Method}
     *
     * @param method the tool {@link Method}
     */
    protected void registerTool(Method method) {
        tools.computeIfAbsent(
                method,
                k -> {
                    ToolDefinition definition = ToolDefinition.from(method);
                    String name = definition.name();
                    String description = definition.description();
                    String jsonSchema = definition.inputSchema();
                    return new DefaultTool(name, description, JsonUtils.toMap(jsonSchema));
                });
    }

    /**
     * get all tools
     */
    public Collection<FunctionTool> getTools() {
        return tools.values();
    }

    /**
     * scan current all method. and determine has annotation {@link org.springframework.ai.tool.annotation.Tool} if it has then register tool.
     */
    void scan() {
        Class<? extends ToolObject> clazz = getClass();
        if (hasScan.compareAndSet(false, true)) {
            Method[] methods = clazz.getMethods();
            Arrays.stream(methods)
                    .filter(method -> method.isAnnotationPresent(org.springframework.ai.tool.annotation.Tool.class))
                    .forEach(this::registerTool);
        }
    }
}
