package cc.allio.turbo.common.db.event;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.function.lambda.*;
import cc.allio.uno.core.reflect.ReflectTools;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 领域事件订阅者
 *
 * @param <D> 领域类型
 * @author j.x
 * @date 2024/1/26 17:12
 * @since 0.1.0
 * @see BehaviorMethodInterceptor
 */
public interface Subscriber<D> extends InitializingBean, DisposableBean,
        DomainEventBusAware, DomainEventBusGetter {

    String INITIALIZE_BEHAVIOR = "initialize";
    String BEFORE = "before";
    String AFTER = "after";
    String DESTROY_BEHAVIOR = "destroy";

    // =================== subscribe method ===================

    /**
     * @see #subscribeOn(String)
     */
    default <T> SingleObservable<D> subscribeOnBefore(ThrowingMethodConsumer<T> eventMethod) {
        String eventName = eventMethod.getMethodName() + StringPool.DASH + BEFORE;
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T> SingleObservable<D> subscribeOnBefore(ThrowingMethodSupplier<T> eventMethod) {
        String eventName = eventMethod.getMethodName() + StringPool.DASH + BEFORE;
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T, R> SingleObservable<D> subscribeOnBefore(ThrowingMethodFunction<T, R> eventMethod) {
        String eventName = eventMethod.getMethodName() + StringPool.DASH + BEFORE;
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2> SingleObservable<D> subscribeOnBefore(ThrowingMethodBiConsumer<T1, T2> eventMethod) {
        String eventName = eventMethod.getMethodName() + StringPool.DASH + BEFORE;
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, R> SingleObservable<D> subscribeOnBefore(ThrowingMethodBiFunction<T1, T2, R> eventMethod) {
        String eventName = eventMethod.getMethodName() + StringPool.DASH + BEFORE;
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, T3> SingleObservable<D> subscribeOnBefore(ThrowingMethodTerConsumer<T1, T2, T3> eventMethod) {
        String eventName = eventMethod.getMethodName() + StringPool.DASH + BEFORE;
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, T3, R> SingleObservable<D> subscribeOnBefore(ThrowingMethodTerFunction<T1, T2, T3, R> eventMethod) {
        String eventName = eventMethod.getMethodName() + StringPool.DASH + BEFORE;
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, T3, T4> SingleObservable<D> subscribeOnBefore(ThrowingMethodQueConsumer<T1, T2, T3, T4> eventMethod) {
        String eventName = eventMethod.getMethodName() + StringPool.DASH + BEFORE;
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, T3, T4, R> SingleObservable<D> subscribeOnBefore(ThrowingMethodQueFunction<T1, T2, T3, T4, R> eventMethod) {
        String eventName = eventMethod.getMethodName() + StringPool.DASH + BEFORE;
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default SingleObservable<D> subscribeOnBefore(String eventMethod) {
        String eventName = eventMethod + StringPool.DASH + BEFORE;
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T> SingleObservable<D> subscribeOn(ThrowingMethodConsumer<T> eventMethod) {
        String eventName = eventMethod.getMethodName();
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T> SingleObservable<D> subscribeOn(ThrowingMethodSupplier<T> eventMethod) {
        String eventName = eventMethod.getMethodName();
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T, R> SingleObservable<D> subscribeOn(ThrowingMethodFunction<T, R> eventMethod) {
        String eventName = eventMethod.getMethodName();
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2> SingleObservable<D> subscribeOn(ThrowingMethodBiConsumer<T1, T2> eventMethod) {
        String eventName = eventMethod.getMethodName();
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, R> SingleObservable<D> subscribeOn(ThrowingMethodBiFunction<T1, T2, R> eventMethod) {
        String eventName = eventMethod.getMethodName();
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, T3> SingleObservable<D> subscribeOn(ThrowingMethodTerConsumer<T1, T2, T3> eventMethod) {
        String eventName = eventMethod.getMethodName();
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, T3, R> SingleObservable<D> subscribeOn(ThrowingMethodTerFunction<T1, T2, T3, R> eventMethod) {
        String eventName = eventMethod.getMethodName();
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, T3, T4> SingleObservable<D> subscribeOn(ThrowingMethodQueConsumer<T1, T2, T3, T4> eventMethod) {
        String eventName = eventMethod.getMethodName();
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, T3, T4, R> SingleObservable<D> subscribeOn(ThrowingMethodQueFunction<T1, T2, T3, T4, R> eventMethod) {
        String eventName = eventMethod.getMethodName();
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T> SingleObservable<D> subscribeOnAfter(ThrowingMethodConsumer<T> eventMethod) {
        String eventName = eventMethod.getMethodName() + StringPool.DASH + AFTER;
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T> SingleObservable<D> subscribeOnAfter(ThrowingMethodSupplier<T> eventMethod) {
        String eventName = eventMethod.getMethodName() + StringPool.DASH + AFTER;
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T, R> SingleObservable<D> subscribeOnAfter(ThrowingMethodFunction<T, R> eventMethod) {
        String eventName = eventMethod.getMethodName() + StringPool.DASH + AFTER;
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2> SingleObservable<D> subscribeOnAfter(ThrowingMethodBiConsumer<T1, T2> eventMethod) {
        String eventName = eventMethod.getMethodName() + StringPool.DASH + AFTER;
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, R> SingleObservable<D> subscribeOnAfter(ThrowingMethodBiFunction<T1, T2, R> eventMethod) {
        String eventName = eventMethod.getMethodName() + StringPool.DASH + AFTER;
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, T3> SingleObservable<D> subscribeOnAfter(ThrowingMethodTerConsumer<T1, T2, T3> eventMethod) {
        String eventName = eventMethod.getMethodName() + StringPool.DASH + AFTER;
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, T3, R> SingleObservable<D> subscribeOnAfter(ThrowingMethodTerFunction<T1, T2, T3, R> eventMethod) {
        String eventName = eventMethod.getMethodName() + StringPool.DASH + AFTER;
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, T3, T4> SingleObservable<D> subscribeOnAfter(ThrowingMethodQueConsumer<T1, T2, T3, T4> eventMethod) {
        String eventName = eventMethod.getMethodName() + StringPool.DASH + AFTER;
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, T3, T4, R> SingleObservable<D> subscribeOnAfter(ThrowingMethodQueFunction<T1, T2, T3, T4, R> eventMethod) {
        String eventName = eventMethod.getMethodName() + StringPool.DASH + AFTER;
        return subscribeOn(eventName);
    }

    /**
     * 基于事件方法的订阅
     *
     * @param event eventMethod
     */
    default SingleObservable<D> subscribeOn(String event) {
        // like DomainName/event
        String path = getDomainName() + StringPool.SLASH + event;
        return new SingleObservable<>(this, path, getDomainEventBus());
    }

    /**
     * bean initialization
     */
    default SingleObservable<D> subscribeOnInitialize() {
        String path = getDomainName() + StringPool.SLASH + INITIALIZE_BEHAVIOR;
        return new SingleObservable<>(this, path, getDomainEventBus());
    }

    /**
     * bean destroy
     */
    default SingleObservable<D> subscribeOnDestroy() {
        String path = getDomainName() + StringPool.SLASH + DESTROY_BEHAVIOR;
        return new SingleObservable<>(this, path, getDomainEventBus());
    }

    /**
     * support to subscribe multi {@link SingleObservable}
     */
    default MultiObservable<D> subscribeOnMultiple(SingleObservable<D>... observable) {
        return new MultiObservable<>(observable);
    }

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
        Class<?> domainType = ReflectTools.getGenericType(this, Subscriber.class);
        if (domainType == null) {
            return null;
        } else {
            return (Class<D>) domainType;
        }
    }

    // =================== lifecycle method ===================

    @Override
    default void afterPropertiesSet() throws Exception {
        try {
            doOnSubscribe();
        } catch (Throwable ex) {
            LoggerFactory.getLogger(this.getClass()).error("subscriber init has error", ex);
            throw new Exception(ex);
        }
        DomainEventBus eventBus = getDomainEventBus();
        if (eventBus != null) {
            // publish
            String path = getDomainName() + StringPool.SLASH + INITIALIZE_BEHAVIOR;
            DomainEventContext eventContext = new DomainEventContext(this, null);
            eventBus.publish(path, eventContext);
        }
    }

    @Override
    default void destroy() throws Exception {
        try {
            doOnDestroy();
        } catch (Throwable ex) {
            LoggerFactory.getLogger(this.getClass()).error("destroy subscriber has error", ex);
            throw new Exception(ex);
        }
        DomainEventBus eventBus = getDomainEventBus();
        if (eventBus != null) {
            // publish
            String path = getDomainName() + StringPool.SLASH + DESTROY_BEHAVIOR;
            DomainEventContext eventContext = new DomainEventContext(this, null);
            eventBus.publish(path, eventContext);
        }
    }

    /**
     * when bean initialization invoke method
     */
    default void doOnSubscribe() throws Throwable {
    }

    /**
     * when bean destroyed invoke method
     */
    default void doOnDestroy() throws Throwable {
    }
}
