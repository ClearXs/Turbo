package cc.allio.turbo.modules.ai.model;

import cc.allio.turbo.modules.ai.runtime.tool.FunctionTool;
import org.springframework.ai.chat.model.ChatModel;

import java.util.Set;

/**
 * spring AI model loader
 *
 * @author j.x
 * @since 0.2.0
 */
public interface ModelLoader {

    /**
     * load {@link ChatModel}
     *
     * @param options the {@link ModelOptions}
     * @param tools   the tools
     * @return
     */
    ChatModel load(ModelOptions options, Set<FunctionTool> tools);
}
