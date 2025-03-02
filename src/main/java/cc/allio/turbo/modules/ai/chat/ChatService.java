package cc.allio.turbo.modules.ai.chat;

import cc.allio.turbo.modules.ai.chat.instruction.Help;
import cc.allio.turbo.modules.ai.chat.instruction.Instruction;
import cc.allio.turbo.modules.ai.chat.memory.MetricSupervisor;
import cc.allio.turbo.modules.ai.chat.message.AdvancedMessage;
import cc.allio.turbo.modules.ai.chat.message.StreamMessage;
import cc.allio.turbo.modules.ai.chat.tool.MethodFunctionTool;
import cc.allio.turbo.modules.ai.model.AgentModel;
import cc.allio.turbo.modules.ai.chat.tool.FunctionTool;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.core.util.JsonUtils;
import com.google.common.collect.Sets;
import jakarta.annotation.Nullable;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.ai.tool.metadata.ToolMetadata;
import org.springframework.ai.tool.method.MethodToolCallback;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.util.*;

/**
 * AI Chat service. support stream chat and call chat.
 *
 * @author j.x
 * @see #call(Prompt, Set, Set)
 * @see #stream(Prompt, Set, Set)
 * @since 0.2.0
 */
public class ChatService {

    private final Map<String, Instruction> instructions;
    private final ChatWithLLM chat;

    public ChatService(AgentModel agentModel) {
        this(agentModel, new InMemoryChatMemory(), UUID.randomUUID().toString(), UUID.randomUUID().toString());
    }

    public ChatService(AgentModel agentModel, ChatMemory chatMemory) {
        this(agentModel, chatMemory, UUID.randomUUID().toString(), UUID.randomUUID().toString());
    }

    public ChatService(AgentModel agentModel,
                       ChatMemory chatMemory,
                       String conversationId) {
        this(agentModel, chatMemory, conversationId, UUID.randomUUID().toString());
    }

    public ChatService(AgentModel agentModel,
                       ChatMemory chatMemory,
                       String conversationId,
                       String sessionId) {
        this.instructions = new HashMap<>();

        Help help = new Help(this);
        instructions.put(help.name(), help);

        this.chat = new ChatWithLLM(agentModel, chatMemory, conversationId, sessionId);
    }

    /**
     * @see #stream(Prompt, Set, Set)
     */
    public Mono<StreamMessage> stream(String... instructions) {
        return stream(null, Sets.newHashSet(instructions));
    }

    /**
     * @see #stream(Prompt, Set, Set)
     */
    public Mono<StreamMessage> stream(Set<String> instructions) {
        return stream(null, instructions);
    }

    /**
     * @see #stream(Prompt, Set, Set)
     */
    public Mono<StreamMessage> stream(@Nullable Prompt prompt, String... instructions) {
        return stream(prompt, Sets.newHashSet(instructions), Collections.emptySet());
    }

    /**
     * @see #stream(Prompt, Set, Set)
     */
    public Mono<StreamMessage> stream(@Nullable Prompt prompt, Set<String> instructions) {
        return stream(prompt, Sets.newHashSet(instructions), Collections.emptySet());
    }

    /**
     * stream chat.
     *
     * @param prompt       the chat agentPrompt
     * @param instructions instruction or user message
     * @param tools        the list of tools
     * @return stream of chat response
     */
    public Mono<StreamMessage> stream(@Nullable Prompt prompt, Set<String> instructions, Set<FunctionTool> tools) {
        return Mono.defer(() -> {
            Set<String> userMessages = new HashSet<>();

            // built in Instruction instance
            List<Instruction> builtinInstruction = new ArrayList<>();
            for (String instructionName : instructions) {
                Instruction builtin = getInstruction(instructionName);
                if (builtin != null) {
                    builtinInstruction.add(builtin);
                } else {
                    userMessages.add(instructionName);
                }
            }

            if (CollectionUtils.isEmpty(builtinInstruction)) {
                return chat.stream(prompt, userMessages, tools);
            }

            return Flux.fromIterable(builtinInstruction)
                    .flatMap(builtin -> builtin.stream(prompt, tools))
                    .collectList()
                    .flatMap(upstream -> {
                        if (CollectionUtils.isEmpty(userMessages)) {
                            return StreamMessage.fromOthers(upstream);
                        }
                        return chat.stream(prompt, userMessages, tools)
                                .doOnNext(streamMessage -> streamMessage.concat(upstream));
                    });
        });
    }

    /**
     * @see #call(Prompt, Set, Set)
     */
    public Flux<AdvancedMessage> call(String... instructions) {
        return call(null, Sets.newHashSet(instructions));
    }

    /**
     * @see #call(Prompt, Set, Set)
     */
    public Flux<AdvancedMessage> call(Set<String> instructions) {
        return call(null, instructions);
    }

    /**
     * @see #call(Prompt, Set, Set)
     */
    public Flux<AdvancedMessage> call(@Nullable Prompt prompt, String... instructions) {
        return call(prompt, Sets.newHashSet(instructions), Collections.emptySet());
    }

    /**
     * @see #call(Prompt, Set, Set)
     */
    public Flux<AdvancedMessage> call(@Nullable Prompt prompt, Set<String> instructions) {
        return call(prompt, instructions, Collections.emptySet());
    }

    /**
     * call model for chat
     *
     * @param prompt       the chat agentPrompt
     * @param instructions instruction or user message
     * @param tools        the list of {@link FunctionTool}
     * @return stream of chat response
     */
    public Flux<AdvancedMessage> call(Prompt prompt, Set<String> instructions, Set<FunctionTool> tools) {
        return Flux.defer(() -> {
            Set<String> userMessages = new HashSet<>();

            List<Instruction> builtinInstruction = new ArrayList<>();
            for (String instructionName : instructions) {
                Instruction builtin = getInstruction(instructionName);
                if (builtin != null) {
                    builtinInstruction.add(builtin);
                } else {
                    userMessages.add(instructionName);
                }
            }

            if (CollectionUtils.isEmpty(builtinInstruction)) {
                return chat.call(prompt, userMessages, tools);
            }

            return Flux.fromIterable(builtinInstruction)
                    .flatMap(builtin -> builtin.call(prompt, tools))
                    .concatWith(chat.call(prompt, userMessages, tools));
        });
    }

    /**
     * get instruction by name
     *
     * @param name the name of instruction
     * @return the instruction or null
     */
    public Instruction getInstruction(String name) {
        return instructions.get(name);
    }


    static class ChatWithLLM {

        private final AgentModel agentModel;
        private final List<Advisor> advisors;
        private final String conversationId;
        private final String sessionId;

        public ChatWithLLM(AgentModel agentModel,
                           ChatMemory chatMemory,
                           String conversationId,
                           String sessionId) {
            this.agentModel = agentModel;
            this.advisors = buildAdvisors(chatMemory);
            this.conversationId = conversationId;
            this.sessionId = sessionId;
        }

        public Mono<StreamMessage> stream(Prompt prompt, Collection<String> userMessages, Set<FunctionTool> tools) {
            return Mono.defer(() -> {
                StreamMessage streamMessage = new StreamMessage();
                return buildRequest(prompt, userMessages, tools)
                        .stream()
                        .chatResponse()
                        .flatMap(response ->
                                Flux.fromIterable(response.getResults())
                                        .map(generation -> AdvancedMessage.fromGeneration(generation, conversationId, sessionId))
                        )
                        .doOnNext(streamMessage::feed)
                        .then(Mono.just(streamMessage));
            });
        }

        public Flux<AdvancedMessage> call(Prompt prompt, Collection<String> userMessages, Set<FunctionTool> tools) {
            return Flux.defer(() -> {
                ChatResponse response = buildRequest(prompt, userMessages, tools).call().chatResponse();
                if (response == null) {
                    return Flux.empty();
                }
                return Flux.fromIterable(response.getResults()).map(generation -> AdvancedMessage.fromGeneration(generation, conversationId, sessionId));
            });
        }

        ChatClient.ChatClientRequestSpec buildRequest(Prompt prompt, Collection<String> userMessages, Set<FunctionTool> tools) {

            ChatModel chatModel = agentModel.getChatModel(tools);
            ChatClient client = ChatClient.create(chatModel);

            List<Message> messages =
                    new ArrayList<>(userMessages.stream().map(UserMessage::new).map(Message.class::cast).toList());

            if (prompt == null) {
                return client.prompt(new Prompt(messages))
                        .tools(getToolsCallbackFromFunctionTool(tools))
                        .advisors(advisors);
            }

            // combine user messages and prompt instructions
            messages.addAll(prompt.getInstructions());

            return client.prompt(new Prompt(messages))
                    .tools(getToolsCallbackFromFunctionTool(tools))
                    .advisors(advisors);
        }

        /**
         * transfer {@link FunctionTool} to {@link MethodToolCallback}
         *
         * @param tools the collection of {@link FunctionTool}
         * @return the list of {@link MethodToolCallback}
         */
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

        List<Advisor> buildAdvisors(ChatMemory chatMemory) {
            MessageChatMemoryAdvisor messageChatMemoryAdvisor = new MessageChatMemoryAdvisor(chatMemory);
            SimpleLoggerAdvisor simpleLoggerAdvisor = new SimpleLoggerAdvisor();
            MetricSupervisor metricSupervisor = new MetricSupervisor();
            return List.of(messageChatMemoryAdvisor, simpleLoggerAdvisor, metricSupervisor);
        }
    }
}
