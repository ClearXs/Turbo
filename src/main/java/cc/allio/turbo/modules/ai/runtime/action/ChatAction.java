package cc.allio.turbo.modules.ai.runtime.action;

import cc.allio.turbo.modules.ai.Input;
import cc.allio.turbo.modules.ai.Output;
import cc.allio.turbo.modules.ai.agent.Agent;
import cc.allio.turbo.modules.ai.agent.AgentPrompt;
import cc.allio.turbo.modules.ai.model.AgentModel;
import cc.allio.turbo.modules.ai.runtime.Environment;
import cc.allio.turbo.modules.ai.runtime.TaskContext;
import cc.allio.uno.core.chain.Chain;
import cc.allio.uno.core.chain.ChainContext;
import cc.allio.uno.core.util.id.IdGenerator;
import org.springframework.ai.chat.prompt.Prompt;
import reactor.core.publisher.Flux;

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
        AgentPrompt prompt = agent.getPrompt(input, environment);
        return model.callChat(prompt)
                .flatMap(responses -> {
                    Output output = new Output();
                    output.setId(IdGenerator.defaultGenerator().getNextId());
                    output.setAgent(agent.name());
                    output.setInputId(input.getId());
                    output.setMessage(responses.getResult().getOutput().getText());
                    taskContext.setOutput(output);
                    return chain.proceed(context);
                });
    }

    @Override
    public String message() {
        return "";
    }

    @Override
    public String getName() {
        return CHAT;
    }
}
