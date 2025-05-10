package cc.allio.turbo.modules.ai.agent;

import cc.allio.turbo.common.domain.Observable;
import cc.allio.turbo.modules.ai.driver.model.Input;
import cc.allio.turbo.modules.ai.driver.model.Output;
import cc.allio.turbo.modules.ai.agent.builtin.chat.ChatAgent;
import cc.allio.turbo.modules.ai.agent.builtin.sma.SMAAgent;
import cc.allio.turbo.modules.ai.chat.tool.FunctionTool;
import cc.allio.turbo.modules.ai.chat.tool.Tool;

import reactor.core.publisher.Mono;

import java.util.Set;

/**
 * definition agent. agent as an enhance AI ability and capability. it is effectively raise system usability.
 * <p>
 * in generally, an agent has three component:
 *    <ol>
 *        <li>planning</li>
 *        <li>tools</li>
 *        <li>memory</li>
 *    </ol>
 * <p>
 *    through task compose actions as a plan, and action will be use tools and memory.
 * </p>
 *
 * @author j.x
 * @since 0.1.1
 */
public interface Agent {

    /**
     * @see ChatAgent
     */
    String CHAT_AGENT = "Chat";

    /**
     * @see SMAAgent
     */
    String SMA_AGENT = "SMA";

    /**
     * accept user input{@link Input}. and generate task for planning and execute current task.
     *
     * @param input the user input
     * @return {@link Observable} for {@link Output}
     */
    Observable<Output> call(Mono<Input> input);

    /**
     * add temporal tool
     *
     * @param tool the {@link FunctionTool}
     */
    void addTemporalTool(FunctionTool tool);

    /**
     * get agent tools
     *
     * @return the set of {@link Tool}
     */
    Set<FunctionTool> getTools();

    /**
     * add building plan action name
     *
     * @param actionName the action name
     */
    void addDispatchActionName(String actionName);

    /**
     * get building plan action names
     *
     * @return the set of name
     */
    Set<String> getDispatchActionNames();

    /**
     * get agent name
     */
    String name();

    /**
     * agent description.
     */
    String description();

    /**
     * get agent prompt template
     */
    String getPromptTemplate();

}
