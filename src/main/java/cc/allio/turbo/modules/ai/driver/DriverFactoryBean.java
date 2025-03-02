package cc.allio.turbo.modules.ai.driver;

import cc.allio.turbo.common.domain.DomainEventContext;
import cc.allio.uno.core.bus.EventBus;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.core.ResolvableType;

public class DriverFactoryBean<D> extends AbstractFactoryBean<Driver<D>> {

    private final Class<D> driverModelClass;
    private final EventBus<DomainEventContext> eventBus;

    public DriverFactoryBean(Class<D> driverModelClass, EventBus<DomainEventContext> eventBus) {
        this.driverModelClass = driverModelClass;
        this.eventBus = eventBus;
    }

    @Override
    public Class<?> getObjectType() {
        return ResolvableType.forClassWithGenerics(Driver.class, driverModelClass).resolve();
    }

    @Override
    protected Driver<D> createInstance() throws Exception {
        return new Driver<>(driverModelClass, eventBus);
    }
}
