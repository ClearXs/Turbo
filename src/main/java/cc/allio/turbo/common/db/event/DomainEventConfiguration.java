package cc.allio.turbo.common.db.event;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainEventConfiguration {

    @Bean
    public DomainEventBus domainEventBus() {
        return new DomainEventBus();
    }

    @Bean
    public BehaviorProcessor behaviorProcessor(DomainEventBus domainEventBus) {
        return new BehaviorProcessor(domainEventBus);
    }
}
