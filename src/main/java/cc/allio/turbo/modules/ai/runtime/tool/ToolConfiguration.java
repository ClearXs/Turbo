package cc.allio.turbo.modules.ai.runtime.tool;

import cc.allio.turbo.modules.ai.runtime.tool.internal.CreateToolObject;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class ToolConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ToolObject createToolObject() {
        return new CreateToolObject();
    }

    @Bean
    public ToolRegistry toolRegistry() {
        return new ToolRegistry();
    }
}
