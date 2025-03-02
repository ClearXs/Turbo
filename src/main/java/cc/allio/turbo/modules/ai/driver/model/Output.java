package cc.allio.turbo.modules.ai.driver.model;

import cc.allio.turbo.modules.ai.driver.DriverModel;
import cc.allio.turbo.modules.ai.enums.MessageStatus;
import cc.allio.turbo.modules.ai.enums.Role;
import cc.allio.turbo.modules.ai.agent.runtime.ExecutionMode;
import cc.allio.uno.core.api.Copyable;
import cc.allio.uno.core.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
@DriverModel
public class Output implements Copyable<Output> {

    private Long id;
    private Long inputId;
    // agent name
    private String agent;

    private long createAt;
    private Role role;
    // the response message
    private String message;
    private ExecutionMode executionMode;

    // message status
    private MessageStatus status;

    @JsonIgnore
    private Input input;

    @Override
    public Output copy() {
        Output output = new Output();

        if (inputId != null) {
            output.setInputId(inputId);
        }

        if (StringUtils.isNotEmpty(agent)) {
            output.setAgent(agent);
        }

        if (executionMode != null) {
            output.setExecutionMode(executionMode);
        }

        return output;
    }
}
