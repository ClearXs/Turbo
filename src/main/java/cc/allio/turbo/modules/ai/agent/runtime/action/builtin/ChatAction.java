package cc.allio.turbo.modules.ai.agent.runtime.action.builtin;

import cc.allio.turbo.modules.ai.driver.model.Input;
import cc.allio.turbo.modules.ai.driver.model.Order;
import cc.allio.turbo.modules.ai.driver.model.Output;
import cc.allio.turbo.modules.ai.agent.Agent;
import cc.allio.turbo.modules.ai.agent.AgentPrompt;
import cc.allio.turbo.modules.ai.chat.ChatService;
import cc.allio.turbo.modules.ai.agent.runtime.action.ActionContext;
import cc.allio.turbo.modules.ai.agent.runtime.action.MessageAction;
import cc.allio.turbo.modules.ai.enums.MessageStatus;
import cc.allio.turbo.modules.ai.chat.message.AdvancedMessage;
import cc.allio.turbo.modules.ai.chat.message.StreamMessage;
import cc.allio.turbo.modules.ai.agent.runtime.Environment;
import cc.allio.turbo.modules.ai.agent.runtime.ExecutionMode;
import cc.allio.turbo.modules.ai.chat.tool.FunctionTool;
import cc.allio.uno.core.chain.Chain;
import cc.allio.uno.core.chain.ChainContext;
import cc.allio.uno.core.util.StringUtils;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

/**
 * implementation model chat action.
 *
 * @author j.x
 * @since 0.2.0
 */
public class ChatAction extends MessageAction {

    private final ChatService chatService;

    public ChatAction(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public Flux<Output> executeMany(Chain<Environment, Output> chain, ChainContext<Environment> context) throws Throwable {
        Environment environment = context.getIN();
        Agent agent = environment.getAgent();
        Input input = environment.getInput();
        AgentPrompt prompt = AgentPrompt.fromAgent(agent, environment);
        Set<FunctionTool> tools = agent.getTools();

        Set<Order> orders = input.getInstructions();
        Flux<Output> out = Flux.empty();
        if (context instanceof ActionContext actionContext) {
            ExecutionMode mode = actionContext.getMode();
            if (mode == ExecutionMode.STREAM) {
                out = chatService.stream(prompt, orders, tools, input.getOptions())
                        .flatMapMany(StreamMessage::observe)
                        .flatMap(new ChatProcess(chain, agent, input, mode, actionContext));
            } else if (mode == ExecutionMode.CALL) {
                out = chatService.call(prompt, orders, tools, input.getOptions())
                        .flatMap(new ChatProcess(chain, agent, input, mode, actionContext));
            }
        } else {
            out = chain.processMany(context);
        }
        return out;
    }

    @AllArgsConstructor
    static class ChatProcess implements Function<AdvancedMessage, Flux<Output>> {

        private final Chain<Environment, Output> chain;
        private final Agent agent;
        private final Input input;
        private final ExecutionMode mode;
        private final ActionContext actionContext;

        @Override
        public Flux<Output> apply(AdvancedMessage message) {
            Output output = new Output();
            output.setId(message.id());
            output.setAgent(agent.name());
            output.setContent(message.content());
            output.setInput(input);
            output.setExecutionMode(mode);
            output.setRole(message.role());
            output.setConversationId(input.getConversationId());
            output.setSessionId(input.getSessionId());
            output.setCreateAt(message.createAt());
            output.setMetadata(message.getMetadata());

            String finish = message.finish();

            if (StringUtils.isBlank(finish)) {
                output.setStatus(MessageStatus.INCOMPLETE);
            } else if (Objects.equals(AdvancedMessage.FINISH_STOP, finish)) {
                output.setStatus(MessageStatus.COMPLETE);
            } else {
                output.setStatus(MessageStatus.ERROR);
            }
            actionContext.addOutput(output);

            return chain.processMany(actionContext);
        }
    }

    @Override
    public String getName() {
        return CHAT;
    }
}
