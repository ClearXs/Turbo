package cc.allio.turbo.common.domain;

import cc.allio.uno.core.bus.EventBus;
import cc.allio.uno.core.bus.TopicKey;
import cc.allio.uno.core.function.lambda.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 领域事件订阅者
 *
 * @param <D> 领域类型
 * @author j.x
 * @date 2024/1/26 17:12
 * @see BehaviorMethodInterceptor
 * @since 0.1.0
 */
public interface Subscriber<D> extends InitializingBean, DisposableBean, Domain<D> {

    // =================== subscribe method ===================

    /**
     * @see #subscribeOn(String)
     */
    default <T> Observable<D> subscribeOnBefore(ThrowingMethodConsumer<T> eventMethod) {
        String eventName = getBeforePath(eventMethod.getMethodName());
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T> Observable<D> subscribeOnBefore(ThrowingMethodSupplier<T> eventMethod) {
        String eventName = getBeforePath(eventMethod.getMethodName());
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T, R> Observable<D> subscribeOnBefore(ThrowingMethodFunction<T, R> eventMethod) {
        String eventName = getBeforePath(eventMethod.getMethodName());
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2> Observable<D> subscribeOnBefore(ThrowingMethodBiConsumer<T1, T2> eventMethod) {
        String eventName = getBeforePath(eventMethod.getMethodName());
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, R> Observable<D> subscribeOnBefore(ThrowingMethodBiFunction<T1, T2, R> eventMethod) {
        String eventName = getBeforePath(eventMethod.getMethodName());
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, T3> Observable<D> subscribeOnBefore(ThrowingMethodTerConsumer<T1, T2, T3> eventMethod) {
        String eventName = getBeforePath(eventMethod.getMethodName());
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, T3, R> Observable<D> subscribeOnBefore(ThrowingMethodTerFunction<T1, T2, T3, R> eventMethod) {
        String eventName = getBeforePath(eventMethod.getMethodName());
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, T3, T4> Observable<D> subscribeOnBefore(ThrowingMethodQueConsumer<T1, T2, T3, T4> eventMethod) {
        String eventName = getBeforePath(eventMethod.getMethodName());
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, T3, T4, R> Observable<D> subscribeOnBefore(ThrowingMethodQueFunction<T1, T2, T3, T4, R> eventMethod) {
        String eventName = getBeforePath(eventMethod.getMethodName());
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default Observable<D> subscribeOnBefore(String eventMethod) {
        String eventName = getBeforePath(eventMethod);
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T> Observable<D> subscribeOn(ThrowingMethodConsumer<T> eventMethod) {
        String eventName = eventMethod.getMethodName();
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T> Observable<D> subscribeOn(ThrowingMethodSupplier<T> eventMethod) {
        String eventName = eventMethod.getMethodName();
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T, R> Observable<D> subscribeOn(ThrowingMethodFunction<T, R> eventMethod) {
        String eventName = eventMethod.getMethodName();
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2> Observable<D> subscribeOn(ThrowingMethodBiConsumer<T1, T2> eventMethod) {
        String eventName = eventMethod.getMethodName();
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, R> Observable<D> subscribeOn(ThrowingMethodBiFunction<T1, T2, R> eventMethod) {
        String eventName = eventMethod.getMethodName();
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, T3> Observable<D> subscribeOn(ThrowingMethodTerConsumer<T1, T2, T3> eventMethod) {
        String eventName = eventMethod.getMethodName();
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, T3, R> Observable<D> subscribeOn(ThrowingMethodTerFunction<T1, T2, T3, R> eventMethod) {
        String eventName = eventMethod.getMethodName();
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, T3, T4> Observable<D> subscribeOn(ThrowingMethodQueConsumer<T1, T2, T3, T4> eventMethod) {
        String eventName = eventMethod.getMethodName();
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, T3, T4, R> Observable<D> subscribeOn(ThrowingMethodQueFunction<T1, T2, T3, T4, R> eventMethod) {
        String eventName = eventMethod.getMethodName();
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T> Observable<D> subscribeOnAfter(ThrowingMethodConsumer<T> eventMethod) {
        String eventName = getAfterPath(eventMethod.getMethodName());
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T> Observable<D> subscribeOnAfter(ThrowingMethodSupplier<T> eventMethod) {
        String eventName = getAfterPath(eventMethod.getMethodName());
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T, R> Observable<D> subscribeOnAfter(ThrowingMethodFunction<T, R> eventMethod) {
        String eventName = getAfterPath(eventMethod.getMethodName());
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2> Observable<D> subscribeOnAfter(ThrowingMethodBiConsumer<T1, T2> eventMethod) {
        String eventName = getAfterPath(eventMethod.getMethodName());
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, R> Observable<D> subscribeOnAfter(ThrowingMethodBiFunction<T1, T2, R> eventMethod) {
        String eventName = getAfterPath(eventMethod.getMethodName());
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, T3> Observable<D> subscribeOnAfter(ThrowingMethodTerConsumer<T1, T2, T3> eventMethod) {
        String eventName = getAfterPath(eventMethod.getMethodName());
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, T3, R> Observable<D> subscribeOnAfter(ThrowingMethodTerFunction<T1, T2, T3, R> eventMethod) {
        String eventName = getAfterPath(eventMethod.getMethodName());
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, T3, T4> Observable<D> subscribeOnAfter(ThrowingMethodQueConsumer<T1, T2, T3, T4> eventMethod) {
        String eventName = getAfterPath(eventMethod.getMethodName());
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, T3, T4, R> Observable<D> subscribeOnAfter(ThrowingMethodQueFunction<T1, T2, T3, T4, R> eventMethod) {
        String eventName = getAfterPath(eventMethod.getMethodName());
        return subscribeOn(eventName);
    }

    /**
     * 基于事件方法的订阅
     *
     * @param event event
     */
    default Observable<D> subscribeOn(String event) {
        return subscribeOn(TopicKey.of(event));
    }

    /**
     * 基于事件方法的订阅
     *
     * @param path the topic path
     */
    default Observable<D> subscribeOn(TopicKey path) {
        TopicKey eventPath = buildEventPath(path);
        return new BehaviorObservable<>(this, eventPath, getDomainEventBus());
    }

    /**
     * bean initialization
     */
    default Observable<D> subscribeOnInitialize() {
        String path = getInitializationPath();
        return new BehaviorObservable<>(this, TopicKey.of(path), getDomainEventBus());
    }

    /**
     * bean destroy
     */
    default Observable<D> subscribeOnDestroy() {
        String path = getDestroyPath();
        return new BehaviorObservable<>(this, TopicKey.of(path), getDomainEventBus());
    }

    /**
     * support to subscribe multi {@link Observable}
     */
    default Observable<D> subscribeOnMultiple(Observable<D>... observable) {
        return new MultiObservable<>(observable);
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
        EventBus<DomainEventContext> eventBus = getDomainEventBus();
        if (eventBus != null) {
            // publish
            String path = getInitializationPath();
            DomainEventContext eventContext = new DomainEventContext(this, null);
            eventBus.publish(path, eventContext).subscribe();
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
        EventBus<DomainEventContext> eventBus = getDomainEventBus();
        if (eventBus != null) {
            // publish
            String path = getDestroyPath();
            DomainEventContext eventContext = new DomainEventContext(this, null);
            eventBus.publish(path, eventContext).subscribe();
        }
    }

    /**
     * when bean initialization withCall method
     */
    default void doOnSubscribe() throws Throwable {
    }

    /**
     * when bean destroyed withCall method
     */
    default void doOnDestroy() throws Throwable {
    }
}
