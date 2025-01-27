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
    default <T> SingleObservable<D> subscribeOnBefore(ThrowingMethodConsumer<T> eventMethod) {
        String eventName = getBeforePath(eventMethod.getMethodName());
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T> SingleObservable<D> subscribeOnBefore(ThrowingMethodSupplier<T> eventMethod) {
        String eventName = getBeforePath(eventMethod.getMethodName());
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T, R> SingleObservable<D> subscribeOnBefore(ThrowingMethodFunction<T, R> eventMethod) {
        String eventName = getBeforePath(eventMethod.getMethodName());
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2> SingleObservable<D> subscribeOnBefore(ThrowingMethodBiConsumer<T1, T2> eventMethod) {
        String eventName = getBeforePath(eventMethod.getMethodName());
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, R> SingleObservable<D> subscribeOnBefore(ThrowingMethodBiFunction<T1, T2, R> eventMethod) {
        String eventName = getBeforePath(eventMethod.getMethodName());
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, T3> SingleObservable<D> subscribeOnBefore(ThrowingMethodTerConsumer<T1, T2, T3> eventMethod) {
        String eventName = getBeforePath(eventMethod.getMethodName());
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, T3, R> SingleObservable<D> subscribeOnBefore(ThrowingMethodTerFunction<T1, T2, T3, R> eventMethod) {
        String eventName = getBeforePath(eventMethod.getMethodName());
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, T3, T4> SingleObservable<D> subscribeOnBefore(ThrowingMethodQueConsumer<T1, T2, T3, T4> eventMethod) {
        String eventName = getBeforePath(eventMethod.getMethodName());
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, T3, T4, R> SingleObservable<D> subscribeOnBefore(ThrowingMethodQueFunction<T1, T2, T3, T4, R> eventMethod) {
        String eventName = getBeforePath(eventMethod.getMethodName());
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default SingleObservable<D> subscribeOnBefore(String eventMethod) {
        String eventName = getBeforePath(eventMethod);
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
        String eventName = getAfterPath(eventMethod.getMethodName());
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T> SingleObservable<D> subscribeOnAfter(ThrowingMethodSupplier<T> eventMethod) {
        String eventName = getAfterPath(eventMethod.getMethodName());
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T, R> SingleObservable<D> subscribeOnAfter(ThrowingMethodFunction<T, R> eventMethod) {
        String eventName = getAfterPath(eventMethod.getMethodName());
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2> SingleObservable<D> subscribeOnAfter(ThrowingMethodBiConsumer<T1, T2> eventMethod) {
        String eventName = getAfterPath(eventMethod.getMethodName());
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, R> SingleObservable<D> subscribeOnAfter(ThrowingMethodBiFunction<T1, T2, R> eventMethod) {
        String eventName = getAfterPath(eventMethod.getMethodName());
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, T3> SingleObservable<D> subscribeOnAfter(ThrowingMethodTerConsumer<T1, T2, T3> eventMethod) {
        String eventName = getAfterPath(eventMethod.getMethodName());
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, T3, R> SingleObservable<D> subscribeOnAfter(ThrowingMethodTerFunction<T1, T2, T3, R> eventMethod) {
        String eventName = getAfterPath(eventMethod.getMethodName());
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, T3, T4> SingleObservable<D> subscribeOnAfter(ThrowingMethodQueConsumer<T1, T2, T3, T4> eventMethod) {
        String eventName = getAfterPath(eventMethod.getMethodName());
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, T3, T4, R> SingleObservable<D> subscribeOnAfter(ThrowingMethodQueFunction<T1, T2, T3, T4, R> eventMethod) {
        String eventName = getAfterPath(eventMethod.getMethodName());
        return subscribeOn(eventName);
    }

    /**
     * 基于事件方法的订阅
     *
     * @param event event
     */
    default SingleObservable<D> subscribeOn(String event) {
        return subscribeOn(TopicKey.of(event));
    }

    /**
     * 基于事件方法的订阅
     *
     * @param topicKey topicKey
     */
    default SingleObservable<D> subscribeOn(TopicKey topicKey) {
        String eventPath = buildEventPath(topicKey.getPath());
        return new SingleObservable<>(this, TopicKey.of(eventPath), getDomainEventBus());
    }

    /**
     * bean initialization
     */
    default SingleObservable<D> subscribeOnInitialize() {
        String path = getInitializationPath();
        return new SingleObservable<>(this, TopicKey.of(path), getDomainEventBus());
    }

    /**
     * bean destroy
     */
    default SingleObservable<D> subscribeOnDestroy() {
        String path = getDestroyPath();
        return new SingleObservable<>(this, TopicKey.of(path), getDomainEventBus());
    }

    /**
     * support to subscribe multi {@link SingleObservable}
     */
    default MultiObservable<D> subscribeOnMultiple(SingleObservable<D>... observable) {
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
     * when bean initialization call method
     */
    default void doOnSubscribe() throws Throwable {
    }

    /**
     * when bean destroyed call method
     */
    default void doOnDestroy() throws Throwable {
    }
}
