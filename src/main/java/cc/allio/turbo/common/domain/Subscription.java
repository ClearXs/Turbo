package cc.allio.turbo.common.domain;

import cc.allio.uno.core.bus.TopicKey;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * 行为订阅信息
 *
 * @author j.x
 * @date 2024/1/26 19:04
 * @since 0.1.0
 */
public class Subscription<D> {

    @Getter
    private final Subscriber<D> subscriber;

    @Getter
    private final TopicKey eventBehavior;
    @Getter
    private final Method behavior;
    private final DomainEventContext eventContext;

    public Subscription(Subscriber<D> subscriber, TopicKey eventBehavior, Method behavior, DomainEventContext eventContext) {
        this.subscriber = subscriber;
        this.eventBehavior = eventBehavior;
        this.behavior = behavior;
        this.eventContext = eventContext;
    }

    /**
     * 获取领域对象
     */
    public Optional<D> getDomain() {
        return eventContext.getDomain();
    }

    /**
     * 获取领域行为结果
     *
     * @return optional
     */
    public Optional<Object> getBehaviorResult() {
        return eventContext.getBehaviorResult();
    }

    /**
     * 获取领域行为结果
     *
     * @param resultType resultType
     * @param <T>        T
     * @return optional
     */
    public <T> Optional<T> getBehaviorResult(Class<T> resultType) {
        return eventContext.getBehaviorResult(resultType);
    }

    /**
     * 根据指定的key获取
     *
     * @param key key
     * @return optional
     */
    public Optional<Object> getParameter(String key) {
        return eventContext.get(key);
    }

    /**
     * 根据指定的key获取
     *
     * @param key key
     * @param classType parameter class
     * @return optional
     * @param <T> parameter type
     */
    public <T> Optional<T> getParameter(String key, Class<T> classType) {
        return eventContext.get(key, classType);
    }
}
