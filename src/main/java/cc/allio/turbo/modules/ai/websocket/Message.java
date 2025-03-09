package cc.allio.turbo.modules.ai.websocket;

import cc.allio.turbo.modules.ai.driver.model.Options;
import cc.allio.turbo.modules.ai.driver.model.Order;
import cc.allio.turbo.modules.ai.model.ModelOptions;
import cc.allio.turbo.modules.ai.agent.runtime.Variable;
import com.google.common.collect.Sets;
import lombok.Data;

import java.util.Set;

@Data
public class Message {

    // the use message
    private Set<Order> instructions;

    // use choose agent
    private String agent;
    private Variable variable;
    private ModelOptions modelOptions;
    private Options options;

    /**
     * create {@link Message} instance
     *
     * @param msg the message
     * @return
     */
    public static Message fromSingle(String msg) {
        Message message = new Message();
        message.setInstructions(Sets.newHashSet(Order.toUser(msg)));
        return message;
    }
}
