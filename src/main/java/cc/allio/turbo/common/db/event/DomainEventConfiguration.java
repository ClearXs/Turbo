package cc.allio.turbo.common.db.event;

import cc.allio.turbo.common.aop.TurboAdvisorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainEventConfiguration {

    @Bean
    public DomainEventBus domainEventBus() {
        return new DomainEventBus();
    }

    @Bean
    public TurboAdvisorBuilder<BehaviorAdvisor> behaviorAdvisorTurboAdvisorBuilder(DomainEventBus eventBus) {
        return BehaviorAdvisor.BehaviorAdvisorBuilder.builder(eventBus);
    }
}
