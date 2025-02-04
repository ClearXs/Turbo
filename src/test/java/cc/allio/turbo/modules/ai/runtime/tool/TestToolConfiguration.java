package cc.allio.turbo.modules.ai.runtime.tool;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;

import java.util.Collections;

@AutoConfiguration
@AutoConfigureBefore(ToolConfiguration.class)
public class TestToolConfiguration {

    @Bean
    public TestToolObject testToolObject() {
        return new TestToolObject();
    }

    @Bean
    public Tool testTool() {
        return new DefaultTool("test", "test", Collections.emptyMap());
    }
}
