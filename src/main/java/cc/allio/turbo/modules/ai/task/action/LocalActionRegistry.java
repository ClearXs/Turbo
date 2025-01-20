package cc.allio.turbo.modules.ai.task.action;

import cc.allio.turbo.modules.ai.resources.AIResources;
import cc.allio.turbo.modules.ai.task.action.internal.CreateAction;
import cc.allio.turbo.modules.ai.task.action.internal.SearchAction;
import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class LocalActionRegistry implements ActionRegistry {

    private final AIResources resources;

    private final Map<String, Action> registry = Maps.newConcurrentMap();
    private final Map<String, Action> internalRegistry = Maps.newConcurrentMap();

    public LocalActionRegistry(AIResources resources) {
        this.resources = resources;
    }

    @Override
    public void lookup() {

        CreateAction createAction = new CreateAction();
        internalRegistry.put(createAction.getName(), createAction);

        SearchAction searchAction = new SearchAction();
        internalRegistry.put(searchAction.getName(), searchAction);

        ChatAction chatAction = new ChatAction();
        registry.put(chatAction.getName(), chatAction);

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

    @Override
    public Optional<Action> getAction(String name) {
        return Optional.of(registry.get(name)).or(() -> Optional.ofNullable(internalRegistry.get(name)));
    }

}
