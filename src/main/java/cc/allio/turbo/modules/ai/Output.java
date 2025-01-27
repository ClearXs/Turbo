package cc.allio.turbo.modules.ai;

import lombok.Data;

@Data
public class Output {

    private Long id;
    private Long inputId;
    // agent name
    private String agent;

    // the response message
    private String message;
}
