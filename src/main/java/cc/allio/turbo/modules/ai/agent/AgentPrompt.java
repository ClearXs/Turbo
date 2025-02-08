package cc.allio.turbo.modules.ai.agent;

import cc.allio.turbo.modules.ai.runtime.Environment;
import cc.allio.turbo.modules.ai.runtime.tool.FunctionTool;
import cc.allio.uno.core.util.StringUtils;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;

import java.util.List;
import java.util.Set;

/**
 * bridge between {@link Prompt} and {@link Agent}
 *
 * @author j.x
 * @since 0.2.0
 */
public class AgentPrompt extends Prompt {

    private final String systemPromptTemplate;
    @Getter
    private final Set<FunctionTool> tools;
    private final Set<String> userMessages;
    private final Environment environment;

    AgentPrompt(String systemPromptTemplate,
                Set<FunctionTool> tools,
                Set<String> userMessages,
                Environment environment) {
        this.systemPromptTemplate = systemPromptTemplate;
        this.tools = tools;
        this.userMessages = userMessages;
        this.environment = environment;
    }

    @Override
    public List<Message> getInstructions() {
        List<Message> messages = Lists.newArrayList();
        if (StringUtils.isNotEmpty(systemPromptTemplate)) {
            // system message
            PromptTemplate systemTemplate = new PromptTemplate(systemPromptTemplate);
            Message systemMessage = new SystemMessage(systemTemplate.render(environment));
            messages.add(systemMessage);
        }
        if (userMessages != null) {
            messages.addAll(
                    userMessages.stream()
                            .map(msg -> {
                                // user message
                                PromptTemplate userTemplate = new PromptTemplate(msg);
                                return userTemplate.createMessage(environment);
                            })
                            .toList()
            );
        }
        return messages;
    }

    /**
     * static method create {@link AgentPrompt} from {@link Agent}
     *
     * @param agent        the agent instance
     * @param userMessages the user message
     * @param environment  the runtime environment.
     * @return {@link AgentPrompt} instance
     */
    public static AgentPrompt fromAgent(Agent agent, Set<String> userMessages, Environment environment) {
        return new AgentPrompt(agent.getPromptTemplate(), agent.getTools(), userMessages, environment);
    }
}
