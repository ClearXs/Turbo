package cc.allio.turbo.modules.ai.resources;

import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class AIResourcesTest extends BaseTestCase {

    AIResources resources = new AIResources();

    @Test
    void testReadAgent() {
        assertDoesNotThrow(() -> resources.read());

        Optional<AIResources.LiteralAgent> agent = resources.detectOfAgent("travel");

        assertTrue(agent.isPresent());

        Optional<AIResources.LiteralAgent> empty = resources.detectOfAgent("empty");

        assertTrue(empty.isEmpty());
    }

}
