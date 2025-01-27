package cc.allio.turbo.modules.ai.runtime.tool;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * model toll
 *
 * @author j.x
 * @since 0.2.0
 */
public interface Tool {

    /**
     * tool type
     */
    Type type();

    @AllArgsConstructor
    @Getter
    enum Type {
        Function("function");
        private final String name;
    }
}
