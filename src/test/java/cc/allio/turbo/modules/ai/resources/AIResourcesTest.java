package cc.allio.turbo.modules.ai.resources;

import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class AIResourcesTest extends BaseTestCase {

    @Test
    void testReadAgent() {
        assertDoesNotThrow(() -> AIResources.getInstance().readAgentFromClasspathResources());

        Optional<AIResources.LiteralAgent> agent = AIResources.getInstance().getAgent("SMA");

        assertTrue(agent.isPresent());
    }

    @Test
    void testReadAction() {
        assertDoesNotThrow(() -> AIResources.getInstance().readActionFromClasspathResources());

        Optional<AIResources.LiteralAction> action = AIResources.getInstance().getAction("create");

        assertTrue(action.isPresent());
    }
}
