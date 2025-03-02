package cc.allio.turbo.modules.ai.model;

import cc.allio.turbo.modules.ai.agent.Agent;
import cc.allio.turbo.modules.ai.agent.AgentPrompt;
import cc.allio.turbo.modules.ai.enums.Role;
import cc.allio.turbo.modules.ai.model.message.Message;
import cc.allio.turbo.modules.ai.model.message.MessageImpl;
import cc.allio.turbo.modules.ai.model.message.StreamMessage;
import cc.allio.turbo.modules.ai.runtime.tool.FunctionTool;
import cc.allio.turbo.modules.ai.runtime.tool.MethodFunctionTool;
import cc.allio.uno.core.util.JsonUtils;
import com.google.common.collect.Maps;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.metadata.ChatGenerationMetadata;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
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
    public Mono<StreamMessage> streamChat(AgentPrompt prompt) {
        return streamChat(prompt, Collections.emptySet());
    }

    /**
     * stream chat.
     *
     * @param prompt the chat agentPrompt
     * @param tools  the list of tools
     * @return stream of chat response
     */
    public Mono<StreamMessage> streamChat(AgentPrompt prompt, Set<FunctionTool> tools) {
        ChatModel chatModel = getChatModel(tools);
        return Mono.defer(() -> {
            StreamMessage streamMessage = new StreamMessage();
            ChatClient.create(chatModel)
                    .prompt(prompt)
                    .tools(getToolsCallbackFromFunctionTool(tools))
                    .stream()
                    .chatResponse()
                    .map(this::handleResponse)
                    .doOnNext(streamMessage::feed)
                    .subscribe();
            return Mono.just(streamMessage);
        });
    }

    /**
     * @see #callChat(AgentPrompt, Set)
     */
    public Flux<Message> callChat(AgentPrompt prompt, Set<String> userMessages) {
        return callChat(prompt, userMessages, Collections.emptySet());
    }

    /**
     * call model for chat
     *
     * @param prompt the chat agentPrompt
     * @param tools  the list of {@link FunctionTool}
     * @return stream of chat response
     */
    public Flux<Message> callChat(AgentPrompt prompt, Set<String> userMessages, Set<FunctionTool> tools) {
        ChatModel chatModel = getChatModel(tools);
        return Flux.defer(() -> {
            ChatResponse response =
                    ChatClient.create(chatModel)
                            .prompt(prompt)
                            .tools(getToolsCallbackFromFunctionTool(tools))
                            .messages()
                            .advisors()
                            .call()
                            .chatResponse();
            return Mono.justOrEmpty(response).map(this::handleResponse);
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
                            .toolDefinition(
                                    ToolDefinition.builder()
                                            .name(functionName)
                                            .description(description)
                                            .inputSchema(JsonUtils.toJson(parameters))
                                            .build()
                            )
                            .toolMetadata(ToolMetadata.from(method))
                            .toolMethod(method)
                            .toolObject(target)
                            .build();
                })
                .toList();
    }

    Message handleResponse(ChatResponse response) {
        Generation generation = response.getResult();
        AssistantMessage output = generation.getOutput();
        ChatGenerationMetadata metadata = generation.getMetadata();
        String finishReason = metadata.getFinishReason();
        MessageImpl message = new MessageImpl();
        message.setFinish(finishReason);
        MessageType messageType = output.getMessageType();
        message.setRole(Role.fromMessageType(messageType));
        message.setCreateAt(message.getCreateAt());
        message.setContent(output.getText());
        return message;
    }
}
