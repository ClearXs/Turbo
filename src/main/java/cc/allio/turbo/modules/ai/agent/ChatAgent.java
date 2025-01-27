package cc.allio.turbo.modules.ai.agent;

import cc.allio.turbo.common.domain.Observable;
import cc.allio.turbo.modules.ai.Driver;
import cc.allio.turbo.modules.ai.Input;
import cc.allio.turbo.modules.ai.Output;
import cc.allio.turbo.modules.ai.exception.AgentInitializationException;
import cc.allio.turbo.modules.ai.resources.AIResources;
import cc.allio.turbo.modules.ai.runtime.Environment;
import cc.allio.turbo.modules.ai.runtime.action.Action;
import cc.allio.turbo.modules.ai.runtime.action.ActionRegistry;

import java.util.List;

public class ChatAgent extends ResourceAgent {

    public ChatAgent(AIResources resources, ActionRegistry actionRegistry, Driver driver) {
        super(resources, actionRegistry, driver);
    }

    @Override
    public Observable<Output> call(Input input) {

        return null;
    }

    @Override
    protected void setup() throws AgentInitializationException {

    }

}
