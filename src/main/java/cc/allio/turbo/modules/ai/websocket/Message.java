package cc.allio.turbo.modules.ai.websocket;

import cc.allio.turbo.modules.ai.model.ModelOptions;
import cc.allio.turbo.modules.ai.agent.runtime.Variable;
import com.google.common.collect.Sets;
import lombok.Data;

import java.util.Set;

@Data
public class Message {

    // the use message
    private Set<String> msgs;

    // use choose agent
    private String agent;
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
        message.setMsgs(Sets.newHashSet(msg));
        return message;
    }
}
