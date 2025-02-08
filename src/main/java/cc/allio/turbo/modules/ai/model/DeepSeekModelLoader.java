package cc.allio.turbo.modules.ai.model;

import cc.allio.turbo.modules.ai.runtime.tool.FunctionTool;
import org.springframework.ai.chat.model.ChatModel;

import java.util.Set;

/**
 * deep seek with open AI have similar API.
 *
 * @author j.x
 * @see OpenAIModelLoader
 * @since 0.2.0
 */
public class DeepSeekModelLoader implements ModelLoader {

    @Override
    public ChatModel load(ModelOptions options, Set<FunctionTool> tools) {
        return AgentModel.MODEL_LOADER_REGISTRY.get(ModelManufacturer.OPENAI).load(options, tools);
    }
}
