package cc.allio.turbo.modules.ai.runtime.action;

import cc.allio.turbo.modules.ai.Input;
import cc.allio.turbo.modules.ai.Output;
import cc.allio.turbo.modules.ai.agent.Agent;
import cc.allio.turbo.modules.ai.agent.AgentPrompt;
import cc.allio.turbo.modules.ai.model.AgentModel;
import cc.allio.turbo.modules.ai.runtime.Environment;
import cc.allio.turbo.modules.ai.runtime.ExecutionMode;
import cc.allio.turbo.modules.ai.runtime.TaskContext;
import cc.allio.turbo.modules.ai.runtime.tool.FunctionTool;
import cc.allio.uno.core.chain.Chain;
import cc.allio.uno.core.chain.ChainContext;
import cc.allio.uno.core.util.id.IdGenerator;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.model.ChatResponse;
import reactor.core.publisher.Flux;

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
    public Flux<Output> executeMany(Chain<TaskContext, Output> chain, ChainContext<TaskContext> context) throws Throwable {
        TaskContext taskContext = context.getIN();
        Agent agent = taskContext.getAgent();
        Input input = taskContext.getInput();
        Environment environment = taskContext.getEnvironment();
        AgentModel model = taskContext.getAgentModel();
        AgentPrompt prompt = AgentPrompt.fromAgent(agent, input.getMessages(), environment);
        Set<FunctionTool> tools = agent.getTools();

        Flux<Output> out = Flux.empty();
        if (context instanceof ActionContext actionContext) {
            ExecutionMode mode = actionContext.getMode();
            if (mode == ExecutionMode.STREAM) {
                out = model.streamChat(prompt, tools).flatMap(new ChatProcess(chain, agent, input, mode, actionContext));
            } else if (mode == ExecutionMode.CALL) {
                out = model.callChat(prompt, tools).flatMap(new ChatProcess(chain, agent, input, mode, actionContext));
            }
        } else {
            out = chain.processMany(context);
        }
        return out;
    }

    @AllArgsConstructor
    static class ChatProcess implements Function<ChatResponse, Flux<Output>> {

        private final Chain<TaskContext, Output> chain;
        private final Agent agent;
        private final Input input;
        private final ExecutionMode mode;
        private final ActionContext actionContext;

        @Override
        public Flux<Output> apply(ChatResponse response) {
            Output output = responseToOutput(response, agent.name(), input, mode);
            actionContext.addOutput(output);
            return chain.processMany(actionContext);
        }

        Output responseToOutput(
                ChatResponse response,
                String agentName,
                Input input,
                ExecutionMode executionMode) {
            Output output = new Output();
            output.setId(IdGenerator.defaultGenerator().getNextId());
            output.setAgent(agentName);
            output.setInputId(input.getId());
            output.setMessage(response.getResult().getOutput().getText());
            output.setInput(input);
            output.setExecutionMode(executionMode);
            return output;
        }

    }

    @Override
    public String getName() {
        return CHAT;
    }
}
