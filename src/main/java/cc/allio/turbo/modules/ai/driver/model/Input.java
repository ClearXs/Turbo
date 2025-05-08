package cc.allio.turbo.modules.ai.driver.model;

import cc.allio.turbo.modules.ai.driver.DriverModel;
import cc.allio.turbo.modules.ai.enums.Role;
import cc.allio.turbo.modules.ai.model.ModelOptions;
import cc.allio.turbo.modules.ai.agent.runtime.ExecutionMode;
import cc.allio.turbo.modules.ai.agent.runtime.Variable;
import cc.allio.turbo.modules.ai.websocket.Message;
import cc.allio.uno.core.api.Copyable;
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

    private String conversationId;
    private String sessionId;
    // the use message
    private Set<Order> instructions = Sets.newHashSet();

    // use choose agents
    private String agent;
    // default use ollama model
    private ModelOptions modelOptions = ModelOptions.getDefaultForLlama();
    // execute model
    private ExecutionMode executionMode = ExecutionMode.STREAM;
    private Options options;

    private Variable variable = new Variable();

    @Override
    public Input copy() {
        Input input = new Input();

        if (StringUtils.isNotEmpty(sessionId)) {
            input.setSessionId(sessionId);
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
        input.setInstructions(message.getInstructions());
        input.setModelOptions(message.getModelOptions());
        input.setVariable(message.getVariable());
        input.setAgent(message.getAgent());
        return input;
    }

    /**
     * add message. default the message role is {@link Role#USER}
     */
    public void addMessage(String message) {
        Order instruction = new Order();
        instruction.setMessage(message);
        instruction.setRole(Role.USER);
        this.instructions.add(instruction);
    }

}
