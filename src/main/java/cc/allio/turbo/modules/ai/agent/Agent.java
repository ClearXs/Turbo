package cc.allio.turbo.modules.ai.agent;

import cc.allio.turbo.common.domain.Observable;
import cc.allio.turbo.modules.ai.Input;
import cc.allio.turbo.modules.ai.Output;
import cc.allio.turbo.modules.ai.exception.AgentInitializationException;
import cc.allio.turbo.modules.ai.resources.AIResources;
import cc.allio.turbo.modules.ai.runtime.Environment;
import cc.allio.turbo.modules.ai.runtime.tool.Tool;

import reactor.core.publisher.Mono;

import java.util.List;

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
     * @return
     */
    Observable<Output> call(Mono<Input> input);

    /**
     * initialization agent and agent will be preparation chain of action.
     */
    void install(AIResources.LiteralAgent literalAgent) throws AgentInitializationException;

    /**
     * get building plan action names
     *
     * @return the list of name
     */
    List<String> getDispatchActionNames();

    /**
     * get agent tools
     *
     * @return the list of {@link Tool}
     */
    List<Tool> getTools();

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
    String getAgentPromptTemplate();

    /**
     * get agent prompt
     *
     * @param input       the user input
     * @param environment the environment
     * @return the {@link AgentPrompt}
     */
    default AgentPrompt getPrompt(Input input, Environment environment) {
        return new AgentPrompt(this, getTools(), input, environment);
    }
}
