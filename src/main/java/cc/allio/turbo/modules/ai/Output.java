package cc.allio.turbo.modules.ai;

import cc.allio.turbo.modules.ai.annotation.DriverModel;
import cc.allio.turbo.modules.ai.runtime.ExecutionMode;
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

    // the response message
    private String message;
    private ExecutionMode executionMode;

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
