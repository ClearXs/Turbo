package cc.allio.turbo.common.db.event;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.function.lambda.*;
import cc.allio.uno.core.util.ReflectTools;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

/**
 * 领域事件订阅者
 *
 * @param <D> 领域类型
 * @author j.x
 * @date 2024/1/26 17:12
 * @since 0.1.0
 */
public interface Subscriber<D> extends ApplicationListener<ApplicationReadyEvent> {

    String INITIALIZE_BEHAVIOR = "initialize";
    String BEFORE = "before";

    /**
     * @see #subscribeOn(String)
     */
    default <T> Observable<D> subscribeOnBefore(ThrowingMethodConsumer<T> eventMethod) {
        String eventName = eventMethod.getMethodName() + StringPool.DASH + BEFORE;
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T> Observable<D> subscribeOnBefore(ThrowingMethodSupplier<T> eventMethod) {
        String eventName = eventMethod.getMethodName() + StringPool.DASH + BEFORE;
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T, R> Observable<D> subscribeOnBefore(ThrowingMethodFunction<T, R> eventMethod) {
        String eventName = eventMethod.getMethodName() + StringPool.DASH + BEFORE;
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2> Observable<D> subscribeOnBefore(ThrowingMethodBiConsumer<T1, T2> eventMethod) {
        String eventName = eventMethod.getMethodName() + StringPool.DASH + BEFORE;
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, R> Observable<D> subscribeOnBefore(ThrowingMethodBiFunction<T1, T2, R> eventMethod) {
        String eventName = eventMethod.getMethodName() + StringPool.DASH + BEFORE;
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, T3> Observable<D> subscribeOnBefore(ThrowingMethodTerConsumer<T1, T2, T3> eventMethod) {
        String eventName = eventMethod.getMethodName() + StringPool.DASH + BEFORE;
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, T3, R> Observable<D> subscribeOnBefore(ThrowingMethodTerFunction<T1, T2, T3, R> eventMethod) {
        String eventName = eventMethod.getMethodName() + StringPool.DASH + BEFORE;
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, T3, T4> Observable<D> subscribeOnBefore(ThrowingMethodQueConsumer<T1, T2, T3, T4> eventMethod) {
        String eventName = eventMethod.getMethodName() + StringPool.DASH + BEFORE;
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default <T1, T2, T3, T4, R> Observable<D> subscribeOnBefore(ThrowingMethodQueFunction<T1, T2, T3, T4, R> eventMethod) {
        String eventName = eventMethod.getMethodName() + StringPool.DASH + BEFORE;
        return subscribeOn(eventName);
    }

    /**
     * @see #subscribeOn(String)
     */
    default Observable<D> subscribeOnBefore(String eventMethod) {
        String eventName = eventMethod + StringPool.DASH + BEFORE;
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
     * 基于事件方法的订阅
     *
     * @param eventMethod eventMethod
     */
    default Observable<D> subscribeOn(String eventMethod) {
        String path = getDomainName() + StringPool.SLASH + eventMethod;
        return new Observable<>(this, path);
    }

    /**
     * 初始化
     */
    default Observable<D> subscribeOnInitialize() {
        String path = getDomainName() + StringPool.SLASH + INITIALIZE_BEHAVIOR;
        return new Observable<>(this, path);
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

    /**
     * 设置{@link Subscriber}的代理对象
     * <p>为什么会存在Proxy方法，因为在Subscriber内部无法使用{@link #subscribeOn(ThrowingMethodConsumer)}等方法，
     * <p>原因是其实现是通过Cglib代理进行实现，故其内部并不是代理类实例，所以无法使用相对的增强。所以通过该方式，在实现类进行实现</p>
     *
     * @param subscriber subscriber
     */
    default void setProxy(Subscriber<D> subscriber) {

    }

    /**
     * 获取{@link Subscriber}代理对象
     *
     * @return Subscriber
     */
    default Subscriber<D> getProxy() {
        return this;
    }


    @Override
    default void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            doOnSubscribe();
            DomainEventBus eventBus = event.getApplicationContext().getBean(DomainEventBus.class);
            String path = getDomainName() + StringPool.SLASH + INITIALIZE_BEHAVIOR;
            DomainEventContext eventContext = new DomainEventContext(this, null);
            eventBus.publish(path, eventContext);
        } catch (Throwable ex) {
            LoggerFactory.getLogger(this.getClass()).error("subscriber init has error", ex);
        }
    }

    /**
     * 该方法内部进行订阅
     */
    default void doOnSubscribe() throws Throwable {

    }
}
