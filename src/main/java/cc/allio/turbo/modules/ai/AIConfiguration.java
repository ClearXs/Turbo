package cc.allio.turbo.modules.ai;

import cc.allio.turbo.common.domain.DomainEventConfiguration;
import cc.allio.turbo.modules.ai.agent.AgentConfiguration;
import cc.allio.turbo.modules.ai.driver.Driver;
import cc.allio.turbo.modules.ai.driver.DriverConfiguration;
import cc.allio.turbo.modules.ai.chat.evaluation.EvaluationController;
import cc.allio.turbo.modules.ai.chat.resources.ResourceConfiguration;
import cc.allio.turbo.modules.ai.agent.runtime.action.ActionConfiguration;
import cc.allio.turbo.modules.ai.chat.tool.ToolConfiguration;
import cc.allio.turbo.modules.ai.driver.model.Output;
import cc.allio.turbo.modules.ai.websocket.WsConfiguration;
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
        DriverConfiguration.class,
        ToolConfiguration.class,
        WsConfiguration.class,
        ActionConfiguration.class,
        AgentConfiguration.class})
@AutoConfigureAfter({DomainEventConfiguration.class, ToolConfiguration.class})
@AutoConfigureBefore(ResourceConfiguration.class)
public class AIConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public EvaluationController evaluationController(@Qualifier("Driver_Output") Driver<Output> driver) {
        return new EvaluationController(driver);
    }
}
