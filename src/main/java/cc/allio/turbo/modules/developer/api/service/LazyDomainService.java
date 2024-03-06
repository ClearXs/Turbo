package cc.allio.turbo.modules.developer.api.service;

import cc.allio.turbo.common.db.uno.repository.ITurboCrudRepository;
import cc.allio.turbo.modules.developer.api.DomainObject;
import cc.allio.turbo.modules.developer.domain.BoSchema;
import cc.allio.uno.core.api.OptionalContext;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.core.function.lambda.ThrowingMethodConsumer;
import cc.allio.uno.core.function.lambda.ThrowingMethodSupplier;
import cc.allio.uno.data.orm.executor.CommandExecutor;

import java.util.Queue;

/**
 * 考虑{@link IDomainService}是一个依赖于外部服务的对象，故做此报装类，需等有时才进行加载
 *
 * @author jiangwei
 * @date 2024/2/28 23:23
 * @since 0.1.1
 */
public class LazyDomainService<T extends DomainObject> implements IDomainService<T> {

    private final ThrowingMethodSupplier<IDomainService<T>> loader;
    private IDomainService<T> actual;

    public LazyDomainService(ThrowingMethodSupplier<IDomainService<T>> loader) {
        this.loader = loader;
    }

    @Override
    public ITurboCrudRepository<T> getRepository() {
        return getActual().getRepository();
    }

    @Override
    public CommandExecutor getExecutor() {
        return getRepository().getExecutor();
    }

    @Override
    public void aspectOn(String domainMethod, ThrowingMethodConsumer<OptionalContext> callback) {
        getActual().aspectOn(domainMethod, callback);
    }

    @Override
    public Queue<DomainAspect> getDomainAspects() {
        return getActual().getDomainAspects();
    }

    @Override
    public BoSchema getBoSchema() {
        return getActual().getBoSchema();
    }

    /**
     * 从loader中获取{@link IDomainService}
     */
    IDomainService<T> getActual() {
        if (loader == null) {
            throw Exceptions.unNull("domain service supply lazy loader is null");
        }
        if (actual == null) {
            try {
                this.actual = loader.get();
            } catch (Throwable ex) {
                throw Exceptions.unchecked(ex);
            }
        }
        return actual;
    }
}
