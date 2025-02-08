package cc.allio.turbo.modules.ai;

import cc.allio.turbo.modules.ai.annotation.DriverModel;
import cc.allio.turbo.modules.ai.model.ModelOptions;
import cc.allio.turbo.modules.ai.runtime.Variable;
import cc.allio.uno.core.api.Copyable;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.core.util.StringUtils;
import com.google.common.collect.Sets;
import lombok.Data;

import java.util.Set;

/**
 * the user input
 */
@Data
@DriverModel
public class Input implements Copyable<Input> {

    private Long id;
    private String sessionId;
    // the use message
    private Set<String> messages;

    // use choose agents
    private Set<String> agents;
    private Variable variable;
    private ModelOptions modelOptions;

    @Override
    public Input copy() {
        Input input = new Input();

        if (StringUtils.isNotEmpty(sessionId)) {
            input.setSessionId(sessionId);
        }

        if (CollectionUtils.isNotEmpty(messages)) {
            input.setMessages(Sets.newHashSet(messages));
        }

        if (CollectionUtils.isNotEmpty(agents)) {
            input.setAgents(Sets.newHashSet(agents));
        }

        if (variable != null) {
            input.setVariable(variable);
        }

        if (modelOptions != null) {
            input.setModelOptions(modelOptions);
        }

        return input;
    }

    /**
     * add user input message
     *
     * @param message the message
     */
    public void addMessage(String message) {
        if (CollectionUtils.isEmpty(messages)) {
            messages = Sets.newHashSet();
        }
        messages.add(message);
    }

    /**
     * add use choose agent
     *
     * @param agent the agent name
     */
    public void addAgent(String agent) {
        if (CollectionUtils.isEmpty(agents)) {
            agents = Sets.newHashSet();
        }
        agents.add(agent);
    }
}
