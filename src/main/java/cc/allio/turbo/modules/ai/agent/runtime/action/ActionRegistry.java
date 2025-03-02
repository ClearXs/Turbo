package cc.allio.turbo.modules.ai.agent.runtime.action;

import cc.allio.turbo.modules.ai.registry.CompositeComponentRegistry;
import cc.allio.turbo.modules.ai.chat.resources.AIResources;

import java.util.Collection;
import java.util.Optional;

/**
 * {@link Action} registry
 *
 * @author j.x
 * @since 0.2.0
 */
public class ActionRegistry extends CompositeComponentRegistry<Action, String> {

    private final AIResources resources;

    public ActionRegistry(AIResources resources) {
        super(Action::getName);
        this.resources = resources;
    }

    @Override
    protected void initialization() throws Exception {
        lookup();
    }

    /**
     * automation lookup action (from resources)
     */
    public void lookup() {
        Collection<AIResources.LiteralAction> actions = resources.getAllAction();
        for (AIResources.LiteralAction literal : actions) {
            getAction(literal.getName())
                    .ifPresent(action -> {
                        if (action instanceof MessageAction messageAction) {
                            messageAction.composeMessage(literal.getContent());
                        }
                    });
        }
    }

    /**
     * according to name get action instance
     *
     * @param name the name of action
     * @return action instance of null
     */
    public Optional<Action> getAction(String name) {
        return Optional.ofNullable(get(name));
    }
}
