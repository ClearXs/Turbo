package cc.allio.turbo.modules.ai.agent;

import cc.allio.turbo.common.event.Observable;
import cc.allio.turbo.modules.ai.Input;
import cc.allio.turbo.modules.ai.task.Response;
import org.springframework.ai.chat.model.ChatModel;

import java.util.List;

/**
 * definition agent. agent as an enhance AI ability and capability. it is effectively raise system usability.
 * <p>
 *    in generally, an agent has three component:
 *    <ol>
 *        <li>planning</li>
 *        <li>tools</li>
 *        <li>memory</li>
 *    </ol>
 *
 *    through task compose actions as a plan, and action will be use tools and memory.
 * </p>
 *
 * @author j.x
 * @since 0.1.1
 */
public interface Agent {

    /**
     * @param input
     * @return
     */
    Observable<Response> invoke(ChatModel model, Input input);

    /**
     * initialization agent and agent will be prepare chain of action.
     */
    void install();

    /**
     * get building plan action names
     *
     * @return the list of name
     */
    List<String> getLiteralActionNames();

    /**
     * get agent name
     */
    String getName();
}
