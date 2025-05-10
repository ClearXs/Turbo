package cc.allio.turbo.modules.ai.chat.resources;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class ResourceConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AIResources resources() {
        return new AIResources();
    }
}
