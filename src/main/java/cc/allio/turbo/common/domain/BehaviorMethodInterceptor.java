package cc.allio.turbo.common.domain;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.bus.EventBus;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 领域方法拦截器，对非{@link Subscriber}的外一切方法进行发布领域事件（如果存在）
 *
 * @author j.x
 * @date 2024/3/5 23:13
 * @since 0.1.1
 */
@Slf4j
public class BehaviorMethodInterceptor implements MethodInterceptor {

    private final Subscriber<?> bean;
    private final EventBus<DomainEventContext> eventBus;

    private static final List<String> DIRECTLY_INVOKE_NAMES =
            List.of("onApplicationEvent",
                    "getDomainType",
                    "subscribeOnBefore",
                    "subscribeOn",
                    "subscribeOnInitialize",
                    "subscribeOnAfter",
                    "subscribeOnMultiple",
                    "setDomainEventBus");

    public BehaviorMethodInterceptor(Subscriber<?> bean, EventBus<DomainEventContext> eventBus) {
        this.bean = bean;
        this.eventBus = eventBus;
    }

    @Nullable
    @Override
    public Object invoke(@NotNull MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        String methodName = method.getName();
        String subscriberName = AopUtils.getTargetClass(bean).getSimpleName();
        if ("getDomainName".equals(methodName)) {
            return subscriberName;
        } else if (DIRECTLY_INVOKE_NAMES.contains(methodName)) {
            return invocation.proceed();
        } else {
            publishOnBehaviorBefore(bean, method, invocation);
            Object result = invocation.proceed();
            publishOnBehavior(bean, method, invocation, result);
            return result;
        }
    }

    /**
     * 在执行领域行为之前发布领域事件
     *
     * @param bean       the {@link Subscriber} instance
     * @param method     the domain behavior method
     * @param invocation aop invocation
     */
    void publishOnBehaviorBefore(Subscriber<?> bean, Method method, MethodInvocation invocation) {
        String subscriberName = AopUtils.getTargetClass(bean).getSimpleName();
        String methodName = method.getName();
        // DomainBehavior/methodName-before
        String beforePath = subscriberName + StringPool.SLASH + methodName + StringPool.DASH + Subscriber.BEFORE;
        if (eventBus != null && eventBus.hasTopic(beforePath)) {
            DomainEventContext eventContext = buildEventContext(bean, method, invocation);
            try {
                eventBus.publish(beforePath, eventContext).subscribe();
            } catch (Throwable ex) {
                log.warn("behavior event path {} subscribe execute has err", beforePath, ex);
            }
        }
    }

    /**
     * 在执行领域行为之后发布领域事件
     *
     * @param bean       the {@link Subscriber} instance
     * @param method     the domain behavior method
     * @param invocation aop invocation
     * @param result     behavior result
     */
    void publishOnBehavior(Subscriber<?> bean, Method method, MethodInvocation invocation, Object result) {
        String subscriberName = AopUtils.getTargetClass(bean).getSimpleName();
        String methodName = method.getName();
        // DomainBehavior/methodName
        String path = subscriberName + StringPool.SLASH + methodName;
        // 需要判断是否存在订阅了领域行为，避免多次订阅信息
        if (eventBus != null && eventBus.hasTopic(path)) {
            // 构建事件上下文
            DomainEventContext eventContext = new ThreadLocalWebDomainEventContext(bean, method);
            Object[] arguments = invocation.getArguments();
            MethodParameterMap methodParameterMap = new MethodParameterMap(method, arguments);
            Class<?> domainType = bean.getDomainType();
            if (domainType != null) {
                // find parameter domain type
                Object parameterDomain = methodParameterMap.getDomain(domainType);
                // if result equals domain type, then put domain
                if (result != null && domainType.isAssignableFrom(result.getClass())) {
                    eventContext.put(DomainEventContext.DOMAIN_KEY, result);
                } else if (parameterDomain != null) {
                    // else set domain
                    eventContext.put(DomainEventContext.DOMAIN_KEY, parameterDomain);
                }
            }
            if (result != null) {
                // set result
                eventContext.put(DomainEventContext.BEHAVIOR_RESULT_KEY, result);
            }
            // put all arguments
            eventContext.putAll(methodParameterMap.getInternalParameters());
            try {
                // publish domain event context in event bus
                eventBus.publish(path, eventContext).subscribe();
            } catch (Throwable ex) {
                log.warn("behavior event path {} subscribe execute has err", path, ex);
            } finally {
                // trigger after behavior
                publishOnBehaviorAfter(bean, method, invocation);
            }
        }
    }

    /**
     * do on domain behavior after execute publish event to event bus
     *
     * @param bean       the {@link Subscriber} instance
     * @param method     the domain behavior method
     * @param invocation aop invocation
     */
    void publishOnBehaviorAfter(Subscriber<?> bean, Method method, MethodInvocation invocation) {
        String subscriberName = AopUtils.getTargetClass(bean).getSimpleName();
        String methodName = method.getName();
        // DomainBehavior/methodName-after
        String afterPath = subscriberName + StringPool.SLASH + methodName + StringPool.DASH + Subscriber.AFTER;
        if (eventBus != null && eventBus.hasTopic(afterPath)) {
            DomainEventContext eventContext = buildEventContext(bean, method, invocation);
            try {
                eventBus.publish(afterPath, eventContext).subscribe();
            } catch (Throwable ex) {
                log.warn("behavior event path {} subscribe execute has err", afterPath, ex);
            }
        }
    }

    /**
     * build newly {@link DomainEventContext}.
     * <p>inject {@link MethodInvocation#getArguments()} set in Context</p>
     * <p>set default key to context. like as  </p>
     *
     * @param bean       the {@link Subscriber} instance
     * @param method     the domain behavior method
     * @param additional the
     * @return the {@link DomainEventContext} instance
     */
    DomainEventContext buildEventContext(Subscriber<?> bean, Method method, MethodInvocation invocation, Consumer<DomainEventContext>... additional) {
        DomainEventContext eventContext = new ThreadLocalWebDomainEventContext(bean, method);
        Object[] arguments = invocation.getArguments();
        MethodParameterMap methodParameterMap = new MethodParameterMap(method, arguments);
        Class<?> domainType = bean.getDomainType();
        // 存放所有参数
        eventContext.putAll(methodParameterMap.getInternalParameters());
        if (domainType != null) {
            Object parameterDomain = methodParameterMap.getDomain(domainType);
            if (parameterDomain != null) {
                eventContext.put(DomainEventContext.DOMAIN_KEY, parameterDomain);
            }
        }
        if (additional != null) {
            for (Consumer<DomainEventContext> extra : additional) {
                extra.accept(eventContext);
            }
        }
        return eventContext;
    }

    static class MethodParameterMap {
        @Getter
        private final Map<String, Object> internalParameters;
        private final Method method;
        private final Object[] arguments;


        public MethodParameterMap(Method method, Object[] arguments) {
            this.internalParameters = Maps.newHashMap();
            this.method = method;
            this.arguments = arguments;
            setupArgument();
        }

        public void setupArgument() {
            if (arguments != null && arguments.length > 0) {
                Parameter[] parameters = method.getParameters();
                for (int i = 0; i < parameters.length; i++) {
                    Parameter parameter = parameters[i];
                    Object argument = arguments[i];
                    if (argument != null) {
                        internalParameters.put(parameter.getName(), argument);
                    }
                }
            }
        }

        /**
         * 从参数中获取领域类型实体
         *
         * @param domainType domainType
         * @return domain instance or null
         */
        public Object getDomain(Class<?> domainType) {
            return internalParameters.values()
                    .stream()
                    .filter(Objects::nonNull)
                    .filter(v -> domainType.isAssignableFrom(v.getClass()))
                    .findFirst()
                    .orElse(null);
        }
    }
}
