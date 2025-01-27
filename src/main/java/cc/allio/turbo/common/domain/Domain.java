package cc.allio.turbo.common.domain;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.bus.EventBus;
import cc.allio.uno.core.reflect.ReflectTools;

/**
 * definition Domain.
 *
 * @author j.x
 * @since 0.2.0
 */
public interface Domain<D> extends DomainEventBusAware, DomainEventBusGetter {

    String INITIALIZE_BEHAVIOR = "initialize";
    String BEFORE = "before";
    String AFTER = "after";
    String DESTROY_BEHAVIOR = "destroy";

    /**
     * 获取订阅者名称
     *
     * @return name
     */
    default String getDomainName() {
        return this.getClass().getSimpleName();
    }

    /**
     * 获取领域类型
     */
    default Class<D> getDomainType() {
        Class<?> domainType = ReflectTools.getGenericType(this, Domain.class);
        if (domainType == null) {
            return null;
        } else {
            return (Class<D>) domainType;
        }
    }

    /**
     * build domain event path. use by {@link Subscriber} {@link Publisher} in {@link EventBus}
     *
     * @param path the original path
     * @return event path
     */
    default String buildEventPath(String path) {
        // like as {DomainName}/{path}
        return getDomainName() + StringPool.SLASH + path;
    }

    /**
     * get domain initialization path. use by {@link Subscriber} {@link Publisher} in {@link EventBus}
     *
     * @return the initialization path
     * @see BehaviorMethodInterceptor
     */
    default String getInitializationPath() {
        return getDomainName() + StringPool.SLASH + INITIALIZE_BEHAVIOR;
    }

    /**
     * get domain call before path. use by {@link Subscriber} {@link Publisher} in {@link EventBus}
     *
     * @param path the original path
     * @return the before path
     * @see BehaviorMethodInterceptor
     */
    default String getBeforePath(String path) {
        // like as {path}-before
        return path + StringPool.DASH + BEFORE;
    }

    /**
     * get domain call after path. use by {@link Subscriber} {@link Publisher} in {@link EventBus}
     *
     * @param path the original path
     * @return the after path
     * @see BehaviorMethodInterceptor
     */
    default String getAfterPath(String path) {
        // like as {path}-before
        return path + StringPool.DASH + AFTER;
    }

    /**
     * get domain call destroy path. use by {@link Subscriber} {@link Publisher} in {@link EventBus}
     *
     * @return the destroy path
     * @see BehaviorMethodInterceptor
     */
    default String getDestroyPath() {
        return getDomainName() + StringPool.SLASH + DESTROY_BEHAVIOR;
    }
}
