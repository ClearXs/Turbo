package cc.allio.turbo.modules.ai.runtime.action;

import cc.allio.turbo.modules.ai.Input;
import cc.allio.turbo.modules.ai.Output;
import cc.allio.turbo.modules.ai.agent.Agent;
import cc.allio.turbo.modules.ai.agent.AgentPrompt;
import cc.allio.turbo.modules.ai.enums.MessageStatus;
import cc.allio.turbo.modules.ai.model.AgentModel;
import cc.allio.turbo.modules.ai.model.message.Message;
import cc.allio.turbo.modules.ai.model.message.StreamMessage;
import cc.allio.turbo.modules.ai.runtime.Environment;
import cc.allio.turbo.modules.ai.runtime.ExecutionMode;
import cc.allio.turbo.modules.ai.runtime.tool.FunctionTool;
import cc.allio.uno.core.chain.Chain;
import cc.allio.uno.core.chain.ChainContext;
import cc.allio.uno.core.util.id.IdGenerator;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.Collections;
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

    @Override
    public Flux<Output> executeMany(Chain<Environment, Output> chain, ChainContext<Environment> context) throws Throwable {
        Environment environment = context.getIN();
        Agent agent = environment.getAgent();
        Input input = environment.getInput();
        AgentModel model = environment.getAgentModel();
        AgentPrompt prompt = AgentPrompt.fromAgent(agent, environment);
        Set<FunctionTool> tools = agent.getTools();

        Flux<Output> out = Flux.empty();

        if (context instanceof ActionContext actionContext) {
            ExecutionMode mode = actionContext.getMode();
            if (mode == ExecutionMode.STREAM) {
                out = model.streamChat(prompt, tools)
                        .flatMapMany(StreamMessage::observe)
                        .flatMap(new ChatProcess(chain, agent, input, mode, actionContext));
            } else if (mode == ExecutionMode.CALL) {
                out = model.callChat(prompt, Collections.emptySet(), tools)
                        .flatMap(new ChatProcess(chain, agent, input, mode, actionContext));
            }
        } else {
            out = chain.processMany(context);
        }
        return out;
    }

    @AllArgsConstructor
    static class ChatProcess implements Function<Message, Flux<Output>> {

        private final Chain<Environment, Output> chain;
        private final Agent agent;
        private final Input input;
        private final ExecutionMode mode;
        private final ActionContext actionContext;

        @Override
        public Flux<Output> apply(Message message) {
            Output output = new Output();

            output.setId(IdGenerator.defaultGenerator().getNextId());
            output.setAgent(agent.name());
            output.setInputId(input.getId());
            output.setMessage(message.content());
            output.setInput(input);
            output.setExecutionMode(mode);
            output.setCreateAt(message.createAt());
            output.setRole(message.role());

            String finish = message.finish();

            if (Objects.equals(Message.UNFINISH, finish)) {
                output.setStatus(MessageStatus.INCOMPLETE);
            } else if (Objects.equals(Message.FINISH_STOP, finish)) {
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
