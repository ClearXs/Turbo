package cc.allio.turbo.modules.ai.runtime.action;

import cc.allio.turbo.modules.ai.AIConfiguration;
import cc.allio.turbo.modules.ai.resources.AIResources;
import cc.allio.turbo.modules.ai.resources.ResourceConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ImportAutoConfiguration(ResourceConfiguration.class)
@AutoConfigureAfter(AIConfiguration.class)
public class ActionConfiguration {

    @Bean
    public Action chatAction() {
        return new ChatAction();
    }

    @Bean
    public Action endAction() {
        return new EndAction();
    }

    @Bean
    public ActionRegistry actionRegistry(AIResources resources) {
        return new ActionRegistry(resources);
    }
}
