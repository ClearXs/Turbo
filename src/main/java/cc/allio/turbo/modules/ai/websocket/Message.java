package cc.allio.turbo.modules.ai.websocket;

import cc.allio.turbo.modules.ai.model.ModelOptions;
import cc.allio.turbo.modules.ai.runtime.Variable;
import lombok.Data;

import java.util.Set;

@Data
public class Message {

    // the use message
    private Set<String> msg;

    // use choose agents
    private Set<String> agents;
    private Variable variable;
    private ModelOptions modelOptions;
}
