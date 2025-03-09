package cc.allio.turbo.modules.ai.model;

import cc.allio.turbo.modules.ai.agent.Agent;
import cc.allio.turbo.modules.ai.chat.tool.FunctionTool;
import com.google.common.collect.Maps;
import org.springframework.ai.chat.model.ChatModel;

import java.util.*;

/**
 * delegate {@link ChatModel} use for {@link Agent}
 *
 * @author j.x
 * @since 0.2.0
 */
public class AgentModel {

    static final Map<ModelManufacturer, ModelLoader> MODEL_LOADER_REGISTRY = Maps.newHashMap();

    static {
        MODEL_LOADER_REGISTRY.put(ModelManufacturer.ANTHROPIC, new AnthropicModelLoader());
        MODEL_LOADER_REGISTRY.put(ModelManufacturer.MONOSHOT, new MonoshotModelLoader());
        MODEL_LOADER_REGISTRY.put(ModelManufacturer.OPENAI, new OpenAIModelLoader());
        MODEL_LOADER_REGISTRY.put(ModelManufacturer.OLLAMA, new OllamaModelLoader());
        MODEL_LOADER_REGISTRY.put(ModelManufacturer.QIAN_FAN, new QianfanModelLoader());
        MODEL_LOADER_REGISTRY.put(ModelManufacturer.ZHIPU, new ZhipuModelLoader());
        MODEL_LOADER_REGISTRY.put(ModelManufacturer.VERTEX, new VertexModelLoader());
        MODEL_LOADER_REGISTRY.put(ModelManufacturer.DEEP_SEEK, new DeepSeekModelLoader());
    }

    private final ModelOptions options;

    public AgentModel(ModelOptions options) {
        this.options = options;
    }

    /**
     * get chat model
     *
     * @param tools the list of tools
     * @return {@link ChatModel}
     * @throws IllegalArgumentException if not found manufacturer
     */
    public ChatModel getChatModel(Set<FunctionTool> tools) {
        ModelManufacturer manufacturer = options.getManufacturer();
        if (manufacturer == null) {
            throw new IllegalArgumentException("manufacture is requirement.");
        }
        ModelLoader modelLoader = MODEL_LOADER_REGISTRY.get(manufacturer);
        return modelLoader.load(options, tools);
    }

    /**
     * use {@link ModelOptions#getDefaultForLlama()} options to create {@link AgentModel}
     */
    public static AgentModel ollama() {
        return new AgentModel(ModelOptions.getDefaultForLlama());
    }
}
