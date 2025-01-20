package cc.allio.turbo.modules.ai.task.action;

import java.util.Optional;

/**
 * the action registry
 *
 * @author j.x
 * @since 0.2.0
 */
public interface ActionRegistry {

    /**
     * automation lookup action (from resources)
     */
    void lookup();

    /**
     * according to name get action instance
     *
     * @param name the name of action
     * @return action instance of null
     */
    Optional<Action> getAction(String name);
}
