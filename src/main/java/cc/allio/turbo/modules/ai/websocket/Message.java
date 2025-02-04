package cc.allio.turbo.modules.ai.websocket;

import cc.allio.turbo.modules.ai.model.ModelOptions;
import lombok.Data;

import java.util.List;

@Data
public class Message {

    // the use message
    private String msg;

    // use choose agents
    private List<String> agents;
    private ModelOptions modelOptions;
}
