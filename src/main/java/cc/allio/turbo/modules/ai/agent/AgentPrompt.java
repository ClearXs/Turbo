package cc.allio.turbo.modules.ai.agent;

import cc.allio.turbo.modules.ai.agent.runtime.Environment;
import cc.allio.uno.core.util.StringUtils;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
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
@AllArgsConstructor
@EqualsAndHashCode(of = {"systemPromptTemplate"}, callSuper = false)
public class AgentPrompt extends Prompt {

    private final String systemPromptTemplate;
    private final Environment environment;

    @Override
    public List<Message> getInstructions() {
        List<Message> messages = Lists.newArrayList();
        if (StringUtils.isNotEmpty(systemPromptTemplate)) {
            // system message
            PromptTemplate systemTemplate = new PromptTemplate(systemPromptTemplate);
            Message systemMessage = new SystemMessage(systemTemplate.render(environment));
            messages.add(systemMessage);
        }
        return messages;
    }

    /**
     * static method create {@link AgentPrompt} from {@link Agent}
     *
     * @see #from(String, Environment)
     */
    public static AgentPrompt fromAgent(Agent agent, Environment environment) {
        return from(agent.getPromptTemplate(), environment);
    }

    /**
     * static method create {@link AgentPrompt}
     *
     * @param systemPromptTemplate the system prompt template
     * @param environment          the runtime environment.
     * @return {@link AgentPrompt} instance
     */
    public static AgentPrompt from(String systemPromptTemplate, Environment environment) {
        return new AgentPrompt(systemPromptTemplate, environment);
    }
}
