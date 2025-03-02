package cc.allio.turbo.modules.ai.chat.tool;

import cc.allio.turbo.modules.ai.chat.resources.AIResources;
import cc.allio.uno.test.BaseTestCase;
import cc.allio.uno.test.Inject;
import cc.allio.uno.test.RunTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RunTest(components = {TestToolConfiguration.class, ToolConfiguration.class})
public class ToolTest extends BaseTestCase {

    @Inject
    public ToolRegistry toolRegistry;

    @Test
    void testTestObjectExistingRegistry() {
        FunctionTool functionTool = toolRegistry.get("getName");
        assertNotNull(functionTool);
    }

    @Test
    void testToolExistingRegistry() {
        FunctionTool functionTool = toolRegistry.get("test");
        assertNotNull(functionTool);
    }

    @Test
    void testFromResourcesRegisterToRegistry() {
        AIResources resources = new AIResources();
        assertDoesNotThrow(resources::readNow);

        Optional<AIResources.LiteralAgent> travelOptional = resources.detectOfAgent("travel");

        assertTrue(travelOptional.isPresent());
        AIResources.LiteralAgent travelAgent = travelOptional.get();

        List<Map<String, Object>> tools = travelAgent.getTools();

        for (Map<String, Object> tool : tools) {
            FunctionTool functionTool = FunctionTool.of(tool);
            assertNotNull(functionTool);
            toolRegistry.put(functionTool.name(), functionTool);
        }

        // verification whether tool function
        FunctionTool functionTool = toolRegistry.get("get_current_weather");

        assertNotNull(functionTool);
    }
}
