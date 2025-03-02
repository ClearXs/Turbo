package cc.allio.turbo.modules.ai.agent;

import cc.allio.turbo.modules.ai.Driver;
import cc.allio.turbo.modules.ai.DriverConfiguration;
import cc.allio.turbo.modules.ai.Input;
import cc.allio.turbo.modules.ai.Output;
import cc.allio.turbo.modules.ai.agent.builtin.AgentBuiltInRegister;
import cc.allio.turbo.modules.ai.resources.AIResources;
import cc.allio.turbo.modules.ai.runtime.action.ActionConfiguration;
import cc.allio.turbo.modules.ai.runtime.action.ActionRegistry;
import cc.allio.turbo.modules.ai.runtime.tool.ToolConfiguration;
import cc.allio.turbo.modules.ai.runtime.tool.ToolRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ImportAutoConfiguration({ToolConfiguration.class, ActionConfiguration.class, DriverConfiguration.class})
@AutoConfigureAfter({DriverConfiguration.class, ToolConfiguration.class, ActionConfiguration.class})
public class AgentConfiguration {

    @Bean
    public AgentBuiltInRegister agentBuiltInRegister(ToolRegistry toolRegistry, ActionRegistry actionRegistry) {
        return new AgentBuiltInRegister(toolRegistry, actionRegistry);
    }

    @Bean
    public AgentRegistry agentRegistry() {
        return new AgentRegistry();
    }

    @Bean
    @ConditionalOnMissingBean
    public Supervisor agentController(@Qualifier("Driver_Input") Driver<Input> inputDriver,
                                      @Qualifier("Driver_Output") Driver<Output> outputDriver,
                                      AgentRegistry agentRegistry,
                                      ActionRegistry actionRegistry,
                                      ToolRegistry toolRegistry,
                                      AIResources resources) {
        return new Supervisor(inputDriver, outputDriver, agentRegistry, actionRegistry, toolRegistry, resources);
    }
}
