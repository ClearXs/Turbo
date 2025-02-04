package cc.allio.turbo.common.domain;

import cc.allio.turbo.common.aop.TurboAdvisorBuilder;
import cc.allio.uno.core.bus.EventBus;
import cc.allio.uno.core.bus.EventBusFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class DomainEventConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public EventBus<DomainEventContext> domainEventBus() {
        return EventBusFactory.current();
    }

    @Bean
    public DomainBeanPostProcessor domainBeanPostProcessor(EventBus<DomainEventContext> eventBus) {
        return new DomainBeanPostProcessor(eventBus);
    }

    @Bean
    public TurboAdvisorBuilder<BehaviorAdvisor> behaviorAdvisorTurboAdvisorBuilder(EventBus<DomainEventContext> eventBus) {
        return BehaviorAdvisor.BehaviorAdvisorBuilder.builder(eventBus);
    }
}
