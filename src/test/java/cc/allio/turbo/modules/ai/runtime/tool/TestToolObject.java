package cc.allio.turbo.modules.ai.runtime.tool;

import org.springframework.ai.tool.annotation.Tool;

public class TestToolObject extends ToolObject {

    @Tool(description = "get today temperature")
    public String getName() {
        return "test";
    }
}
