package cc.allio.turbo.modules.ai.agent;

import cc.allio.turbo.modules.ai.Input;
import cc.allio.turbo.modules.ai.runtime.Environment;
import cc.allio.turbo.modules.ai.runtime.tool.Tool;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;

import java.util.List;

/**
 * bridge between {@link Prompt} and {@link Agent}
 *
 * @author j.x
 * @since 0.2.0
 */
public class AgentPrompt extends Prompt {

    private final Agent agent;
    @Getter
    private final List<Tool> tools;
    private final Input input;
    private final Environment environment;

    public AgentPrompt(Agent agent,
                       List<Tool> tools,
                       Input input,
                       Environment environment) {
        this.agent = agent;
        this.tools = tools;
        this.input = input;
        this.environment = environment;
    }

    @Override
    public List<Message> getInstructions() {
        // system message
        PromptTemplate systemTemplate = new PromptTemplate(agent.getAgentPromptTemplate());
        SystemMessage systemMessage = new SystemMessage(systemTemplate.render(environment));
        // user message
        PromptTemplate userTemplate = new PromptTemplate(input.getMessage());
        Message userMessage = userTemplate.createMessage(environment);
        return Lists.newArrayList(systemMessage, userMessage);
    }

}
