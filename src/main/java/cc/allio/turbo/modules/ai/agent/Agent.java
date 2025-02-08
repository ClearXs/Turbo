package cc.allio.turbo.modules.ai.agent;

import cc.allio.turbo.common.domain.Observable;
import cc.allio.turbo.modules.ai.Input;
import cc.allio.turbo.modules.ai.Output;
import cc.allio.turbo.modules.ai.exception.AgentInitializationException;
import cc.allio.turbo.modules.ai.resources.AIResources;
import cc.allio.turbo.modules.ai.runtime.ExecutionMode;
import cc.allio.turbo.modules.ai.runtime.tool.FunctionTool;
import cc.allio.turbo.modules.ai.runtime.tool.Tool;

import reactor.core.publisher.Mono;

import java.util.List;
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
     * accept user input{@link Input}. and generate task for planning and execute current task.
     *
     * @param input the user input
     * @param mode
     * @return
     */
    Observable<Output> call(Mono<Input> input, ExecutionMode mode);

    /**
     * initialization agent and agent will be preparation chain of action.
     */
    void install(AIResources.LiteralAgent literalAgent) throws AgentInitializationException;

    /**
     * get agent tools
     *
     * @return the list of {@link Tool}
     */
    Set<FunctionTool> getTools();

    /**
     * get building plan action names
     *
     * @return the list of name
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
