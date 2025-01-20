package cc.allio.turbo.modules.developer.api;

import cc.allio.turbo.common.event.DomainEventBus;
import cc.allio.turbo.modules.developer.api.service.DomainServiceRegistry;
import cc.allio.turbo.modules.developer.api.service.DomainServiceRegistryImpl;
import cc.allio.turbo.modules.developer.service.IDevBoService;
import cc.allio.turbo.modules.developer.service.IDevDataSourceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfiguration {

    @Bean
    public DomainServiceRegistry domainServiceRegistry(IDevBoService devBoService, IDevDataSourceService dataSourceService, DomainEventBus domainEventBus) {
        DomainServiceRegistry domainServiceRegistry = new DomainServiceRegistryImpl(devBoService, dataSourceService, domainEventBus);
        DomainServiceRegistryImpl.INSTANCE = domainServiceRegistry;
        return domainServiceRegistry;
    }
}
