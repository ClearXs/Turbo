package cc.allio.turbo.modules.ai.agent.runtime.action;

import cc.allio.turbo.modules.ai.driver.model.Output;
import cc.allio.turbo.modules.ai.agent.runtime.Environment;
import cc.allio.uno.core.chain.Node;

/**
 * construct a part of plan.
 *
 * <p>
 * it can consist of execute chain do plan for a task. this actions maybe a chat action, a search action, a crate action etc.
 * </p>
 *
 * @author j.x
 * @since 0.2.0
 */
public interface Action extends Node<Environment, Output> {

    // default action names
    String END = "end";
    String CHAT = "chat";

    /**
     * get action name
     */
    String getName();
}
