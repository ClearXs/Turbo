package cc.allio.turbo.modules.ai.runtime.action;

import cc.allio.turbo.modules.ai.Input;
import cc.allio.turbo.modules.ai.Output;
import cc.allio.turbo.modules.ai.agent.Agent;
import cc.allio.turbo.modules.ai.model.AgentModel;
import cc.allio.turbo.modules.ai.model.ModelOptions;
import cc.allio.turbo.modules.ai.runtime.Environment;
import cc.allio.turbo.modules.ai.runtime.ExecutionMode;
import cc.allio.uno.core.chain.Chain;
import cc.allio.uno.core.chain.DefaultChain;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.test.StepVerifier;

import java.util.List;

public class ChatActionTest extends BaseTestCase {

    Chain<Environment, Output> chain;

    Agent agent;
    AgentModel agentModel;

    @Override
    public void onInit() {
        ChatAction chatAction = new ChatAction();
        chain = new DefaultChain<>(List.of(chatAction));
        agent = Mockito.mock(Agent.class);

        Mockito.when(agent.name()).thenReturn("test");

        agentModel = new AgentModel(ModelOptions.getDefaultForOllama());
    }

    @Test
    void testStream() {
        Environment taskEnvironment = new Environment();
        taskEnvironment.setAgentModel(agentModel);
        taskEnvironment.setAgent(agent);
        Input input = new Input();
        input.setMessage("hello");

        taskEnvironment.setInput(input);

        chain.processMany(new ActionContext(taskEnvironment, ExecutionMode.STREAM))
                .take(1)
                .as(StepVerifier::create)
                .expectNextCount(1L)
                .verifyComplete();
    }
}
