package cc.allio.turbo.modules.development.api;

import cc.allio.turbo.common.domain.DomainEventContext;
import cc.allio.turbo.modules.development.api.service.DomainServiceRegistry;
import cc.allio.turbo.modules.development.api.service.DomainServiceRegistryImpl;
import cc.allio.turbo.modules.development.service.IDevBoService;
import cc.allio.turbo.modules.development.service.IDevDataSourceService;
import cc.allio.uno.core.bus.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfiguration {

    @Bean
    public DomainServiceRegistry domainServiceRegistry(IDevBoService devBoService,
                                                       IDevDataSourceService dataSourceService,
                                                       EventBus<DomainEventContext> domainEventBus) {
        DomainServiceRegistry domainServiceRegistry = new DomainServiceRegistryImpl(devBoService, dataSourceService, domainEventBus);
        DomainServiceRegistryImpl.INSTANCE = domainServiceRegistry;
        return domainServiceRegistry;
    }
}
