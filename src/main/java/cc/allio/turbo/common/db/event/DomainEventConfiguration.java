package cc.allio.turbo.common.db.event;

import cc.allio.turbo.common.aop.TurboAdvisorBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainEventConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DomainEventBus domainEventBus() {
        return new DomainEventBus();
    }

    @Bean
    public DomainBeanPostProcessor domainBeanPostProcessor(DomainEventBus eventBus) {
        return new DomainBeanPostProcessor(eventBus);
    }

    @Bean
    public TurboAdvisorBuilder<BehaviorAdvisor> behaviorAdvisorTurboAdvisorBuilder(DomainEventBus eventBus) {
        return BehaviorAdvisor.BehaviorAdvisorBuilder.builder(eventBus);
    }
}
