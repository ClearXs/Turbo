package cc.allio.turbo.modules.ai.runtime.action;

import cc.allio.turbo.modules.ai.CompositeComponentRegistry;
import cc.allio.turbo.modules.ai.resources.AIResources;

import java.util.Collection;
import java.util.Optional;

public class ActionRegistry extends CompositeComponentRegistry<Action, String> {

    private final AIResources resources;

    public ActionRegistry(AIResources resources) {
        super(Action::getName);
        this.resources = resources;
    }

    /**
     * automation lookup action (from resources)
     */
   public void lookup() {
       ChatAction chatAction = new ChatAction();
       put(chatAction.getName(), chatAction);
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
