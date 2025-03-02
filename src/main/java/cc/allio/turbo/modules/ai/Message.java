package cc.allio.turbo.modules.ai;

import cc.allio.turbo.modules.ai.model.ModelOptions;
import cc.allio.turbo.modules.ai.runtime.Variable;
import lombok.Data;

import java.util.Set;

@Data
public class Message {

    // the use message
    private String msg;

    // use choose agents
    private Set<String> agents;
    private Variable variable;
    private ModelOptions modelOptions;

    /**
     * create {@link Message} instance
     *
     * @param msg the message
     * @return
     */
    public static Message fromSingle(String msg) {
        Message message = new Message();
        message.setMsg(msg);
        return message;
    }
}
