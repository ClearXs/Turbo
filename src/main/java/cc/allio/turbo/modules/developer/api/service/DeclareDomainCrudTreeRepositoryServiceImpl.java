package cc.allio.turbo.modules.developer.api.service;

import cc.allio.turbo.common.event.DomainEventBus;
import cc.allio.turbo.common.db.uno.repository.ITurboCrudRepository;
import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCurdRepositoryImpl;
import cc.allio.turbo.modules.developer.api.DomainObject;
import cc.allio.turbo.modules.developer.api.GeneralDomainObject;
import cc.allio.turbo.modules.developer.domain.BoSchema;
import cc.allio.uno.core.api.OptionalContext;
import cc.allio.uno.core.function.lambda.ThrowingMethodConsumer;

import java.util.*;

/**
 * 声明式领域服务，对{@link DomainCrudTreeRepositoryServiceImpl}的装饰。
 *
 * @author j.x
 * @date 2024/2/28 19:51
 * @since 0.1.1
 */
public class DeclareDomainCrudTreeRepositoryServiceImpl<T extends DomainObject> implements IDomainService<T> {

    private final IDomainService<GeneralDomainObject> actual;
    private final Class<T> domainObjectClass;
    private ITurboCrudRepository<T> actualRepository;

    public DeclareDomainCrudTreeRepositoryServiceImpl(IDomainService<GeneralDomainObject> actual, Class<T> domainObjectClass) {
        this.actual = actual;
        this.domainObjectClass = domainObjectClass;
    }

    @Override
    public ITurboCrudRepository<T> getRepository() {
        synchronized (this) {
            if (actualRepository == null) {
                actualRepository = new SimpleTurboCurdRepositoryImpl<>(actual.getExecutor(), domainObjectClass);
            }
        }
        return actualRepository;
    }

    @Override
    public void aspectOn(String domainMethod, ThrowingMethodConsumer<OptionalContext> callback) {
        actual.aspectOn(domainMethod, callback);
    }

    @Override
    public Queue<DomainAspect> getDomainAspects() {
        return actual.getDomainAspects();
    }

    @Override
    public BoSchema getBoSchema() {
        return actual.getBoSchema();
    }

    @Override
    public Class<T> getEntityType() {
        return domainObjectClass;
    }

    @Override
    public void setDomainEventBus(DomainEventBus eventBus) {
        getRepository().setDomainEventBus(eventBus);
    }

    @Override
    public DomainEventBus getDomainEventBus() {
        return getRepository().getDomainEventBus();
    }
}
