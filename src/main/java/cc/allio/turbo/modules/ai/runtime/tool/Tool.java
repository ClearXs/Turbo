package cc.allio.turbo.modules.ai.runtime.tool;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * model tool
 *
 * @author j.x
 * @since 0.2.0
 */
public interface Tool {

    String FUNCTION_KEY = "function";

    /**
     * tool type
     */
    Type type();

    @AllArgsConstructor
    @Getter
    enum Type {
        Function(FUNCTION_KEY);
        private final String name;
    }
}
