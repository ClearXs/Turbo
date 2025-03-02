package cc.allio.turbo.modules.ai;

import cc.allio.turbo.modules.ai.annotation.DriverModel;
import cc.allio.turbo.modules.ai.enums.Role;
import cc.allio.turbo.modules.ai.model.ModelOptions;
import cc.allio.turbo.modules.ai.runtime.ExecutionMode;
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
    private String message;

    // use choose agents
    private Set<String> agents;
    private Variable variable;
    private ModelOptions modelOptions;
    private Role role = Role.USER;
    // execute model
    ExecutionMode executionMode = ExecutionMode.STREAM;

    @Override
    public Input copy() {
        Input input = new Input();

        if (StringUtils.isNotEmpty(sessionId)) {
            input.setSessionId(sessionId);
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

    /**
     * add variable
     *
     * @param key   the key
     * @param value the value
     */
    public void addVariable(String key, Object value) {
        if (variable == null) {
            variable = new Variable();
        }
        variable.put(key, value);
    }

    /**
     * create {@link Input} instance from {@link Message}
     *
     * @param message the {@link Message} instance
     * @return the {@link Input} instance
     */
    public static Input fromMessage(Message message) {
        Input input = new Input();
        input.setMessage(message.getMsg());
        input.setModelOptions(message.getModelOptions());
        input.setVariable(message.getVariable());
        input.setAgents(message.getAgents());
        return input;
    }
}
