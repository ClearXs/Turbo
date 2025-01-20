package cc.allio.turbo.modules.ai.task.action;

import cc.allio.turbo.modules.ai.task.Response;
import cc.allio.turbo.modules.ai.task.TaskContext;
import cc.allio.uno.core.chain.Node;

/**
 * construct a part of plan.
 *
 * <p>
 *     it can consist of execute chain do plan for a task. this actions maybe a chat action, a search action, a crate action etc.
 * </p>
 *
 * @author j.x
 * @since 0.2.0
 */
public interface Action extends Node<TaskContext, Response> {

    /**
     * get action information of message
     */
    String message();

    /**
     * get action name
     */
    String getName();
}
