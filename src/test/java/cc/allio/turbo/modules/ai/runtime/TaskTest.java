package cc.allio.turbo.modules.ai.runtime;

import cc.allio.turbo.modules.ai.Input;
import cc.allio.turbo.modules.ai.Output;
import cc.allio.turbo.modules.ai.agent.Agent;
import cc.allio.turbo.modules.ai.model.ModelManufacturer;
import cc.allio.turbo.modules.ai.model.ModelOptions;
import cc.allio.turbo.modules.ai.runtime.action.ActionConfiguration;
import cc.allio.turbo.modules.ai.runtime.action.ActionRegistry;
import cc.allio.turbo.modules.ai.runtime.action.TestAction;
import cc.allio.uno.core.chain.Chain;
import cc.allio.uno.core.chain.Node;
import cc.allio.uno.test.BaseTestCase;
import cc.allio.uno.test.Inject;
import cc.allio.uno.test.RunTest;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

@RunTest(components = {ActionConfiguration.class, TestAction.class})
public class TaskTest extends BaseTestCase {

    @Inject
    private ActionRegistry actionRegistry;
    private Agent mockAgent;
    private Task task;

    private Input templateInput;

    private static final String AGENT_NAME = "test";
    private static final String AGENT_DESCRIPTION = "this is test agent";
    private static final String AGENT_PROMPT_TEMPLATE = "you are english teacher";

    @Override
    public void onInit() throws Throwable {
        mockAgent = Mockito.mock(Agent.class);
        Mockito.when(mockAgent.name()).thenReturn(AGENT_NAME);
        Mockito.when(mockAgent.description()).thenReturn(AGENT_DESCRIPTION);
        Mockito.when(mockAgent.getPromptTemplate()).thenReturn(AGENT_PROMPT_TEMPLATE);


        this.task = new Task(mockAgent, actionRegistry);
        this.templateInput = new Input();

        ModelOptions modelOptions = new ModelOptions();
        modelOptions.setAddress("http://localhost:11434");
        modelOptions.setModel("llama3.2:1b");
        modelOptions.setManufacturer(ModelManufacturer.OLLAMA);

        templateInput.setModelOptions(modelOptions);
    }

    @Test
    void testEnvironment() {
        Environment environment = task.getEnvironment();

        assertNotNull(environment);
        Object agentName = environment.get(Environment.AGENT_NAME);
        assertEquals(AGENT_NAME, agentName);
    }

    @Test
    void testBuildPlanning() {
        Chain<TaskContext, Output> chain = task.buildPlaning();
        List<? extends Node<TaskContext, Output>> nodes = chain.getNodes();

        assertEquals(2, nodes.size());
    }

    @Test
    void testPlanningOfEnd() {
        task.execute(Mono.just(templateInput))
                .observeMany()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    void testPlanningOfLocalChat() {
        Mockito.when(mockAgent.getDispatchActionNames()).thenReturn(Sets.newHashSet("chat"));
        Input input = templateInput.copy();
        input.addMessage("what's english grammar?");

        task.execute(Mono.just(input), ExecutionMode.CALL)
                .observeMany()
                .as(StepVerifier::create)
                .expectNextCount(1L)
                .verifyComplete();
    }

}
