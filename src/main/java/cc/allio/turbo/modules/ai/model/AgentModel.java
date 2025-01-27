package cc.allio.turbo.modules.ai.model;

import cc.allio.turbo.modules.ai.agent.Agent;
import cc.allio.turbo.modules.ai.agent.AgentPrompt;
import com.google.common.collect.Maps;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import reactor.core.publisher.Flux;

import java.util.Map;

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
    }

    private final ModelOptions options;

    public AgentModel(ModelOptions options) {
        this.options = options;
    }

    /**
     * call model for chat
     *
     * @param prompt the chat agentPrompt
     * @return stream of chat response
     */
    public Flux<ChatResponse> callChat(AgentPrompt prompt) {
        ModelManufacturer manufacturer = options.getManufacturer();
        if (manufacturer == null) {
            throw new IllegalArgumentException("manufacture is requirement.");
        }
        ModelLoader modelLoader = MODEL_LOADER_REGISTRY.get(manufacturer);
        return modelLoader.load(options, prompt.getTools()).stream(prompt);
    }

}
