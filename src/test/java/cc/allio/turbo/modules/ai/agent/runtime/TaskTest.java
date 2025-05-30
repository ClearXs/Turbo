package cc.allio.turbo.modules.ai.agent.runtime;

import cc.allio.turbo.modules.ai.driver.model.Input;
import cc.allio.turbo.modules.ai.driver.model.Output;
import cc.allio.turbo.modules.ai.agent.Agent;
import cc.allio.turbo.modules.ai.model.AgentModel;
import cc.allio.turbo.modules.ai.model.ModelManufacturer;
import cc.allio.turbo.modules.ai.model.ModelOptions;
import cc.allio.turbo.modules.ai.agent.runtime.action.ActionConfiguration;
import cc.allio.turbo.modules.ai.agent.runtime.action.ActionRegistry;
import cc.allio.turbo.modules.ai.agent.runtime.action.TestAction;
import cc.allio.turbo.modules.ai.chat.tool.FunctionTool;
import cc.allio.turbo.modules.ai.chat.tool.TestToolObject;
import cc.allio.turbo.modules.ai.store.InMemoryChatMessageStore;
import cc.allio.uno.core.chain.Chain;
import cc.allio.uno.core.chain.Node;
import cc.allio.uno.test.BaseTestCase;
import cc.allio.uno.test.Inject;
import cc.allio.uno.test.RunTest;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Set;

@RunTest(components = {ActionConfiguration.class, TestAction.class})
public class TaskTest extends BaseTestCase {

    @Inject
    private ActionRegistry actionRegistry;
    private Agent mockTestAgent;

    private Input templateInput;

    private static final String TEST_AGENT_NAME = "test";
    private static final String TEST_AGENT_DESCRIPTION = "this is test agent";
    private static final String TEST_AGENT_PROMPT_TEMPLATE = "you are english teacher";

    private Agent mockTemperatureAgent;
    private static final String TEMPERATURE_AGENT_NAME = "temperature";
    private static final String TEMPERATURE_AGENT_DESCRIPTION = "this is temperature agent";
    private static final String TEMPERATURE_AGENT_PROMPT_TEMPLATE = "you are temperature agent";

    @Override
    public void onInit() throws Throwable {
        mockTestAgent = Mockito.mock(Agent.class);
        Mockito.when(mockTestAgent.name()).thenReturn(TEST_AGENT_NAME);
        Mockito.when(mockTestAgent.description()).thenReturn(TEST_AGENT_DESCRIPTION);
        Mockito.when(mockTestAgent.getPromptTemplate()).thenReturn(TEST_AGENT_PROMPT_TEMPLATE);

        this.templateInput = new Input();

        ModelOptions modelOptions = new ModelOptions();
        modelOptions.setAddress("http://localhost:11434");
        modelOptions.setModel("llama3.2:1b");
        modelOptions.setManufacturer(ModelManufacturer.OLLAMA);

        templateInput.setModelOptions(modelOptions);

        // build temperature agent
        mockTemperatureAgent = Mockito.mock(Agent.class);
        Mockito.when(mockTemperatureAgent.name()).thenReturn(TEMPERATURE_AGENT_NAME);
        Mockito.when(mockTemperatureAgent.description()).thenReturn(TEMPERATURE_AGENT_DESCRIPTION);
        Mockito.when(mockTemperatureAgent.getPromptTemplate()).thenReturn(TEMPERATURE_AGENT_PROMPT_TEMPLATE);
        TestToolObject testToolObject = new TestToolObject();
        testToolObject.afterPropertiesSet();
        Set<FunctionTool> tools = testToolObject.getTools();
        Mockito.when(mockTemperatureAgent.getTools()).thenReturn(tools);
    }

    @Test
    void testEnvironment() {
        Environment environment = new Task(mockTestAgent, actionRegistry, new InMemoryChatMessageStore()).getEnvironment();

        assertNotNull(environment);
        Object agentName = environment.get(Environment.AGENT_NAME);
        assertEquals(TEST_AGENT_NAME, agentName);
    }

    @Test
    void testBuildPlanning() {

        Input input = Mockito.mock(Input.class);
        Mockito.when(input.getSessionId()).thenReturn("1");
        Mockito.when(input.getConversationId()).thenReturn("2");
        AgentModel agentModel = Mockito.mock(AgentModel.class);
        Environment environment = new Environment();
        Chain<Environment, Output> chain =
                new Task(mockTestAgent, actionRegistry, new InMemoryChatMessageStore())
                        .buildPlaning(agentModel, input, environment);
        List<? extends Node<Environment, Output>> nodes = chain.getNodes();

        assertEquals(3, nodes.size());
    }

    @Test
    void testPlanningOfEnd() {
        new Task(mockTestAgent, actionRegistry, new InMemoryChatMessageStore())
                .execute(Mono.just(templateInput))
                .observeMany()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    void testPlanningOfLocalChat() {
        Mockito.when(mockTestAgent.getDispatchActionNames()).thenReturn(Sets.newHashSet("chat"));
        Input input = templateInput.copy();
        input.addMessage("what's english grammar?");

        new Task(mockTestAgent, actionRegistry, new InMemoryChatMessageStore())
                .execute(Mono.just(input))
                .observeMany()
                .as(StepVerifier::create)
                .expectNextCount(1L)
                .verifyComplete();
    }

    @Test
    void testPlanningOfToolChat() {
        Mockito.when(mockTemperatureAgent.getDispatchActionNames()).thenReturn(Sets.newHashSet("chat"));
        Input input = templateInput.copy();
        input.addMessage("what's today temperature?");

        new Task(mockTemperatureAgent, actionRegistry, new InMemoryChatMessageStore())
                .execute(Mono.just(input))
                .observeMany()
                .flatMap(subscription -> Mono.justOrEmpty(subscription.getDomain()))
                .map(Output::getContent)
                .as(StepVerifier::create)
                .expectNextCount(1L)
                .verifyComplete();
    }

}
