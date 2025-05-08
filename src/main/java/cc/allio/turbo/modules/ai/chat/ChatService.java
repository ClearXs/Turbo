package cc.allio.turbo.modules.ai.chat;

import cc.allio.turbo.modules.ai.chat.exception.ChatException;
import cc.allio.turbo.modules.ai.chat.instruction.Help;
import cc.allio.turbo.modules.ai.chat.instruction.Instruction;
import cc.allio.turbo.modules.ai.chat.memory.MetricSupervisor;
import cc.allio.turbo.modules.ai.chat.memory.SessionInMemoryChatMemory;
import cc.allio.turbo.modules.ai.chat.message.AdvancedMessage;
import cc.allio.turbo.modules.ai.chat.message.StreamMessage;
import cc.allio.turbo.modules.ai.chat.tool.MethodFunctionTool;
import cc.allio.turbo.modules.ai.driver.model.Options;
import cc.allio.turbo.modules.ai.driver.model.Order;
import cc.allio.turbo.modules.ai.enums.Role;
import cc.allio.turbo.modules.ai.model.AgentModel;
import cc.allio.turbo.modules.ai.chat.tool.FunctionTool;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.core.util.JsonUtils;
import com.google.common.collect.Sets;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.ai.tool.metadata.ToolMetadata;
import org.springframework.ai.tool.method.MethodToolCallback;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.util.*;

/**
 * AI Chat service. support withStream chat and withCall chat.
 *
 * @author j.x
 * @see #call(Prompt, Set, Set, Options)
 * @see #stream(Prompt, Set, Set, Options)
 * @since 0.2.0
 */
public class ChatService {

    private final Map<String, Instruction> instructions;
    private final ChatWithLLM chat;

    public ChatService(AgentModel agentModel) {
        this(agentModel, new SessionInMemoryChatMemory(UUID.randomUUID().toString()), UUID.randomUUID().toString(), UUID.randomUUID().toString());
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
     * @see #stream(Prompt, Set, Set, Options)
     */
    public Mono<StreamMessage> stream(Order... orders) {
        return stream(null, Sets.newHashSet(orders));
    }

    /**
     * @see #stream(Prompt, Set, Set, Options)
     */
    public Mono<StreamMessage> stream(Set<Order> orders) {
        return stream(null, orders);
    }

    /**
     * @see #stream(Prompt, Set, Set, Options)
     */
    public Mono<StreamMessage> stream(@Nullable Prompt prompt, Order... orders) {
        return stream(prompt, Sets.newHashSet(orders), Collections.emptySet(), new Options());
    }

    /**
     * @see #stream(Prompt, Set, Set, Options)
     */
    public Mono<StreamMessage> stream(@Nullable Prompt prompt, Set<Order> orders) {
        return stream(prompt, Sets.newHashSet(orders), Collections.emptySet(), new Options());
    }

    /**
     * withStream chat.
     *
     * @param prompt  the chat agentPrompt
     * @param orders  orders
     * @param tools   the list of tools
     * @param options the chat with llm options
     * @return withStream of chat response
     */
    public Mono<StreamMessage> stream(@Nullable Prompt prompt, Set<Order> orders, Set<FunctionTool> tools, Options options) {
        return Mono.defer(() -> {
            Set<Order> messages = new HashSet<>();
            // built in Instruction instance
            List<Instruction> builtinInstruction = new ArrayList<>();
            for (Order order : orders) {
                Instruction builtin = getInstruction(order.getMessage());
                if (order.getRole() == Role.INSTRUCTION && builtin != null) {
                    builtinInstruction.add(builtin);
                } else {
                    messages.add(order);
                }
            }

            if (CollectionUtils.isEmpty(builtinInstruction)) {
                return chat.withStream(prompt, messages, tools, options);
            }

            return Flux.fromIterable(builtinInstruction)
                    .flatMap(builtin -> builtin.stream(prompt, tools))
                    .collectList()
                    .flatMap(upstream -> {
                        if (CollectionUtils.isEmpty(messages)) {
                            return StreamMessage.fromOthers(upstream);
                        }
                        return chat.withStream(prompt, messages, tools, options)
                                .doOnNext(streamMessage -> streamMessage.concat(upstream));
                    });
        });
    }

    /**
     * @see #call(Prompt, Set, Set, Options)
     */
    public Flux<AdvancedMessage> call(Order... orders) {
        return call(null, Sets.newHashSet(orders));
    }

    /**
     * @see #call(Prompt, Set, Set, Options)
     */
    public Flux<AdvancedMessage> call(Set<Order> orders) {
        return call(null, orders);
    }

    /**
     * @see #call(Prompt, Set, Set, Options)
     */
    public Flux<AdvancedMessage> call(@Nullable Prompt prompt, Order... orders) {
        return call(prompt, Sets.newHashSet(orders), Collections.emptySet(), new Options());
    }

    /**
     * @see #call(Prompt, Set, Set, Options)
     */
    public Flux<AdvancedMessage> call(@Nullable Prompt prompt, Set<Order> orders) {
        return call(prompt, orders, Collections.emptySet(), new Options());
    }

    /**
     * withCall model for chat
     *
     * @param prompt the chat agentPrompt
     * @param orders instruction or user message
     * @param tools  the list of {@link FunctionTool}
     * @return withStream of chat response
     */
    public Flux<AdvancedMessage> call(Prompt prompt,
                                      Set<Order> orders,
                                      Set<FunctionTool> tools,
                                      Options options) {
        return Flux.defer(() -> {
            Set<Order> messages = new HashSet<>();

            List<Instruction> builtinInstruction = new ArrayList<>();
            for (Order order : orders) {
                Instruction builtin = getInstruction(order.getMessage());
                if (order.getRole() == Role.INSTRUCTION && builtin != null) {
                    builtinInstruction.add(builtin);
                } else {
                    messages.add(order);
                }
            }

            if (CollectionUtils.isEmpty(builtinInstruction)) {
                return chat.withCall(prompt, messages, tools, options);
            }

            return Flux.fromIterable(builtinInstruction)
                    .flatMap(builtin -> builtin.call(prompt, tools))
                    .concatWith(chat.withCall(prompt, messages, tools, options));
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

    @Slf4j
    static class ChatWithLLM {

        private final AgentModel agentModel;
        private final String conversationId;
        private final String sessionId;
        private final ChatMemory chatMemory;

        public ChatWithLLM(AgentModel agentModel,
                           ChatMemory chatMemory,
                           String conversationId,
                           String sessionId) {
            this.agentModel = agentModel;
            this.chatMemory = chatMemory;
            this.conversationId = conversationId;
            this.sessionId = sessionId;
        }

        public Mono<StreamMessage> withStream(Prompt prompt,
                                              Collection<Order> messages,
                                              Set<FunctionTool> tools,
                                              Options options) {
            return Mono.defer(() -> {
                StreamMessage streamMessage = new StreamMessage();
                ChatClient.ChatClientRequestSpec request = buildRequest(prompt, messages, tools, options);
                return request
                        .stream()
                        .chatResponse()
                        .flatMap(response ->
                                Flux.fromIterable(response.getResults())
                                        .map(generation -> AdvancedMessage.fromGeneration(generation, conversationId, sessionId))
                        )
                        .doOnNext(streamMessage::feed)
                        .then(Mono.just(streamMessage))
                        .onErrorMap(ChatException::new);
            });
        }

        public Flux<AdvancedMessage> withCall(Prompt prompt,
                                              Collection<Order> messages,
                                              Set<FunctionTool> tools,
                                              Options options) {
            return Flux.defer(() -> {
                ChatClient.ChatClientRequestSpec request = buildRequest(prompt, messages, tools, options);
                ChatResponse response = request.call().chatResponse();
                if (response == null) {
                    return Flux.empty();
                }
                List<Generation> results = response.getResults();
                return Flux.fromIterable(results)
                        .map(generation -> AdvancedMessage.fromGeneration(generation, conversationId, sessionId))
                        .onErrorMap(ChatException::new);
            });
        }

        ChatClient.ChatClientRequestSpec buildRequest(Prompt prompt,
                                                      Collection<Order> messages,
                                                      Set<FunctionTool> tools,
                                                      Options options) {
            ChatModel chatModel = agentModel.getChatModel(tools);
            ChatClient client = ChatClient.create(chatModel);
            List<Message> callMessages = new ArrayList<>();
            for (Order message : messages) {
                if (message.getRole() == Role.USER) {
                    callMessages.add(new UserMessage(message.getMessage()));
                } else if (message.getRole() == Role.SYSTEM) {
                    SystemMessage systemMessage = new SystemMessage(message.getMessage());
                    callMessages.add(systemMessage);
                }
            }

            // build advisor
            List<Advisor> advisors = buildAdvisors(chatMemory, options);

            if (prompt == null) {
                return client.prompt(new Prompt(callMessages))
                        .tools(getToolsCallbackFromFunctionTool(tools))
                        .advisors(advisors);
            }

            // combine user instructions and prompt instructions
            callMessages.addAll(prompt.getInstructions());

            return client.prompt(new Prompt(callMessages))
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

        /**
         * add chat with LLM request and response advisors.
         * <p>
         * generally, add {@link MessageChatMemoryAdvisor}, {@link SimpleLoggerAdvisor} and {@link MetricSupervisor}
         * <p>
         * TODO consider RAG knowledge how add to chat with LLM
         *
         * @param chatMemory use for build chat memory messages in one conversation.
         * @param options    build advisor options
         * @return
         */
        List<Advisor> buildAdvisors(ChatMemory chatMemory, Options options) {

            // build memory
            int chatHistoryWindowSize = options.isEnableLimitHistoryMessages() ? options.getMaxHistoryMessageNums() : 0;
            MessageChatMemoryAdvisor messageChatMemoryAdvisor =
                    new MessageChatMemoryAdvisor(chatMemory, conversationId, chatHistoryWindowSize);

            // build log
            SimpleLoggerAdvisor simpleLoggerAdvisor = new SimpleLoggerAdvisor();

            // build metric token
            MetricSupervisor metricSupervisor = new MetricSupervisor();

            return List.of(messageChatMemoryAdvisor, simpleLoggerAdvisor, metricSupervisor);
        }
    }
}
