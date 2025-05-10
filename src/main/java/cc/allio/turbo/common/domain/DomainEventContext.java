package cc.allio.turbo.common.domain;

import cc.allio.uno.core.bus.DefaultEventContext;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * 领域事件上下文
 *
 * @author j.x
 * @date 2024/1/26 15:03
 * @since 0.1.0
 */
public class DomainEventContext extends DefaultEventContext {

    public static final String DOMAIN_KEY = "domain_key";
    // 领域事件行为结果
    public static final String BEHAVIOR_RESULT_KEY = "behavior_result_key";
    @Getter
    Method behavior;

    final Domain<?> domain;

    public DomainEventContext(Domain<?> domain) {
        super();
        this.domain = domain;
        setDomainLoad(domain);
    }

    public DomainEventContext(Domain<?> subscriber, Method behavior) {
        super();
        this.behavior = behavior;
        this.domain = subscriber;
        setDomainLoad(domain);
    }

    void setDomainLoad(Domain<?> domain) {
        if (domain instanceof GeneralDomain<?> generalDomain) {
            put(DOMAIN_KEY, generalDomain.getLoad());
        }
    }

    /**
     * 获取领域对象
     *
     * @return optional
     */
    public <T> Optional<T> getDomain() {
        return (Optional<T>) get(DOMAIN_KEY, domain.getDomainType());
    }

    /**
     * 获取领域行为结果
     *
     * @return optional
     */
    public Optional<Object> getBehaviorResult() {
        return get(BEHAVIOR_RESULT_KEY);
    }

    /**
     * 获取领域行为结果
     *
     * @param resultType resultType
     * @param <T>        T
     * @return optional
     */
    public <T> Optional<T> getBehaviorResult(Class<T> resultType) {
        return get(BEHAVIOR_RESULT_KEY, resultType);
    }
}
