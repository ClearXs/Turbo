package cc.allio.turbo.modules.ai.runtime.tool;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
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
        tools.computeIfAbsent(method, k -> MethodFunctionTool.from(method, this));
    }

    /**
     * get all tools
     */
    public Set<FunctionTool> getTools() {
        return Sets.newHashSet(tools.values());
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
