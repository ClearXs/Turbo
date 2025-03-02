package cc.allio.turbo.modules.ai.chat.tool;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class ToolConfiguration {

    @Bean
    public ToolRegistry toolRegistry() {
        return new ToolRegistry();
    }
}
