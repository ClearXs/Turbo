package cc.allio.turbo.modules.ai;

import cc.allio.turbo.common.domain.DomainEventConfiguration;
import cc.allio.turbo.common.domain.DomainEventContext;
import cc.allio.uno.core.bus.EventBus;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ImportAutoConfiguration(DomainEventConfiguration.class)
public class DriverConfiguration {

    @Bean
    public DriverBeanRegister driverBeanRegister(EventBus<DomainEventContext> eventBus) {
        return new DriverBeanRegister(eventBus);
    }
}
