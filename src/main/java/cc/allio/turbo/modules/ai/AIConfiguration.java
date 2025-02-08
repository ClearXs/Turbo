package cc.allio.turbo.modules.ai;

import cc.allio.turbo.common.domain.DomainEventConfiguration;
import cc.allio.turbo.common.domain.DomainEventContext;
import cc.allio.turbo.modules.ai.agent.AgentController;
import cc.allio.turbo.modules.ai.agent.AgentRegistry;
import cc.allio.turbo.modules.ai.evaluation.EvaluationController;
import cc.allio.turbo.modules.ai.resources.AIResources;
import cc.allio.turbo.modules.ai.resources.ResourceConfiguration;
import cc.allio.turbo.modules.ai.runtime.action.ActionConfiguration;
import cc.allio.turbo.modules.ai.runtime.action.ActionRegistry;
import cc.allio.turbo.modules.ai.runtime.tool.ToolConfiguration;
import cc.allio.turbo.modules.ai.runtime.tool.ToolRegistry;
import cc.allio.turbo.modules.ai.websocket.AIWsConfiguration;
import cc.allio.uno.core.bus.EventBus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.*;

@AutoConfiguration
@ImportAutoConfiguration({
        ResourceConfiguration.class,
        DomainEventConfiguration.class,
        ToolConfiguration.class,
        AIWsConfiguration.class,
        ActionConfiguration.class})
@AutoConfigureAfter({DomainEventConfiguration.class, ToolConfiguration.class})
@AutoConfigureBefore(ResourceConfiguration.class)
public class AIConfiguration {

    @Bean
    public AgentRegistry agentRegistry() {
        return new AgentRegistry();
    }

    @Bean
    public DriverBeanRegister driverBeanRegister(EventBus<DomainEventContext> eventBus) {
        return new DriverBeanRegister(eventBus);
    }

    @Bean
    @ConditionalOnMissingBean
    public AgentController agentController(@Qualifier("Driver_Input") Driver<Input> driver,
                                           AgentRegistry agentRegistry,
                                           ActionRegistry actionRegistry,
                                           ToolRegistry toolRegistry,
                                           AIResources resources) {
        return new AgentController(driver, agentRegistry, actionRegistry, toolRegistry, resources);
    }

    @Bean
    @ConditionalOnMissingBean
    public EvaluationController evaluationController(@Qualifier("Driver_Output") Driver<Output> driver) {
        return new EvaluationController(driver);
    }
}
