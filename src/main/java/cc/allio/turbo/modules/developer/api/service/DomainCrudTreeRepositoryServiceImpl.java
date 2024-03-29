package cc.allio.turbo.modules.developer.api.service;

import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudTreeRepositoryServiceImpl;
import cc.allio.turbo.modules.developer.api.DomainObject;
import cc.allio.turbo.modules.developer.domain.BoSchema;
import cc.allio.uno.core.api.OptionalContext;
import cc.allio.uno.core.function.lambda.ThrowingMethodConsumer;
import cc.allio.uno.data.orm.executor.AggregateCommandExecutor;
import com.google.common.collect.Queues;

import java.util.Queue;

/**
 * {@link IDomainService}的默认实现
 *
 * @author j.x
 * @date 2024/2/28 19:44
 * @since 0.1.1
 */
public class DomainCrudTreeRepositoryServiceImpl<T extends DomainObject> extends SimpleTurboCrudTreeRepositoryServiceImpl<T> implements IDomainService<T> {

    private final Queue<DomainAspect> domainAspects;
    private final BoSchema schema;

    public DomainCrudTreeRepositoryServiceImpl(AggregateCommandExecutor commandExecutor, BoSchema schema, Class<T> domainObjectClass) {
        super(commandExecutor, domainObjectClass);
        this.domainAspects = Queues.newConcurrentLinkedQueue();
        this.schema = schema;
    }

    @Override
    public void aspectOn(String domainMethod, ThrowingMethodConsumer<OptionalContext> callback) {
        this.domainAspects.offer(new DomainInspectImpl(domainMethod, callback));
    }

    @Override
    public Queue<DomainAspect> getDomainAspects() {
        return domainAspects;
    }

    @Override
    public BoSchema getBoSchema() {
        return schema;
    }
}
