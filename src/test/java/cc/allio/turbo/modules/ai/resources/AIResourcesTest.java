package cc.allio.turbo.modules.ai.resources;

import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class AIResourcesTest extends BaseTestCase {

    AIResources resources = new AIResources();

    @Test
    void testReadAgent() {
        assertDoesNotThrow(() -> resources.readNow());

        Optional<AIResources.LiteralAgent> agent = resources.getAgent("travel");

        assertTrue(agent.isPresent());

        Optional<AIResources.LiteralAgent> empty = resources.getAgent("empty");

        assertTrue(empty.isEmpty());
    }

}
