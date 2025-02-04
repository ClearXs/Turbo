package cc.allio.turbo.modules.ai;

import cc.allio.turbo.modules.ai.annotation.DriverModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
@DriverModel
public class Output {

    private Long id;
    private Long inputId;
    // agent name
    private String agent;

    // the response message
    private String message;

    @JsonIgnore
    private Input input;
}
