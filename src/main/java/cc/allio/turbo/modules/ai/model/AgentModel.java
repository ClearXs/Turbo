package cc.allio.turbo.modules.ai.model;

import cc.allio.turbo.modules.ai.agent.Agent;
import cc.allio.turbo.modules.ai.agent.AgentPrompt;
import cc.allio.turbo.modules.ai.runtime.tool.FunctionTool;
import cc.allio.turbo.modules.ai.runtime.tool.MethodFunctionTool;
import cc.allio.uno.core.util.JsonUtils;
import com.google.common.collect.Maps;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.ai.tool.metadata.ToolMetadata;
import org.springframework.ai.tool.method.MethodToolCallback;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
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
     * @see #streamChat(AgentPrompt, Set)
     */
    public Flux<ChatResponse> streamChat(AgentPrompt prompt) {
        return streamChat(prompt, Collections.emptySet());
    }

    /**
     * stream chat.
     *
     * @param prompt the chat agentPrompt
     * @param tools  the list of tools
     * @return stream of chat response
     */
    public Flux<ChatResponse> streamChat(AgentPrompt prompt, Set<FunctionTool> tools) {
        ChatModel chatModel = getChatModel(tools);
        return ChatClient.create(chatModel)
                .prompt(prompt)
                .tools(getToolsCallbackFromFunctionTool(tools))
                .stream()
                .chatResponse();
    }

    /**
     * @see #callChat(AgentPrompt, Set)
     */
    public Flux<ChatResponse> callChat(AgentPrompt prompt) {
        return callChat(prompt, Collections.emptySet());
    }

    /**
     * call model for chat
     *
     * @param prompt the chat agentPrompt
     * @param tools  the list of {@link FunctionTool}
     * @return stream of chat response
     */
    public Flux<ChatResponse> callChat(AgentPrompt prompt, Set<FunctionTool> tools) {
        ChatModel chatModel = getChatModel(tools);
        return Flux.defer(() -> {
            ChatResponse response =
                    ChatClient.create(chatModel)
                            .prompt(prompt)
                            .tools(getToolsCallbackFromFunctionTool(tools))
                            .call()
                            .chatResponse();
            return Mono.justOrEmpty(response);
        });
    }


    /**
     * get chat model
     *
     * @param tools the list of tools
     * @return {@link ChatModel}
     * @throws IllegalArgumentException if not found manufacturer
     */
    ChatModel getChatModel(Set<FunctionTool> tools) {
        ModelManufacturer manufacturer = options.getManufacturer();
        if (manufacturer == null) {
            throw new IllegalArgumentException("manufacture is requirement.");
        }
        ModelLoader modelLoader = MODEL_LOADER_REGISTRY.get(manufacturer);
        return modelLoader.load(options, tools);
    }

    List<MethodToolCallback> getToolsCallbackFromFunctionTool(Collection<FunctionTool> tools) {
        return tools.stream()
                .filter(t -> MethodFunctionTool.class.isAssignableFrom(t.getClass()))
                .map(MethodFunctionTool.class::cast)
                .map(methodFunctionTool -> {

                    String functionName = methodFunctionTool.name();
                    String description = methodFunctionTool.description();
                    Map<String, Object> parameters = methodFunctionTool.parameters();

                    Method method = methodFunctionTool.getMethod();
                    Object target = methodFunctionTool.getTarget();
                    return MethodToolCallback.builder()
                            .toolDefinition(ToolDefinition.builder().name(functionName).description(description).inputSchema(JsonUtils.toJson(parameters)).build())
                            .toolMetadata(ToolMetadata.from(method))
                            .toolMethod(method)
                            .toolObject(target)
                            .build();
                })
                .toList();
    }
}
