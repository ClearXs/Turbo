package cc.allio.turbo.modules.ai.agent;

import cc.allio.turbo.modules.ai.agent.builtin.chat.ChatAgent;
import cc.allio.turbo.modules.ai.agent.builtin.sma.SMAAgent;
import cc.allio.uno.test.BaseTestCase;
import cc.allio.uno.test.Inject;
import cc.allio.uno.test.RunTest;
import org.junit.jupiter.api.Test;

@RunTest(components = AgentConfiguration.class)
public class AgentBuiltInRegisterTest extends BaseTestCase {

    @Inject
    private ChatAgent chatAgent;

    @Inject
    private SMAAgent smaAgent;

    @Inject
    private AgentRegistry agentRegistry;

    @Test
    void testWhetherScanCorrect() {
        assertNotNull(chatAgent);
        assertNotNull(smaAgent);
        assertNotNull(agentRegistry);

        assertEquals(chatAgent, agentRegistry.get(chatAgent.name()));
        assertEquals(smaAgent, agentRegistry.get(smaAgent.name()));
    }
}
