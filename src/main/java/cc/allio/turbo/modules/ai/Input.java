package cc.allio.turbo.modules.ai;

import cc.allio.turbo.modules.ai.model.ModelOptions;
import lombok.Data;

import java.util.List;

/**
 * the user input
 */
@Data
public class Input {

    private Long id;
    // the use message
    private String message;

    // use choose agents
    private List<String> agents;
    private ModelOptions modelOptions;
}
