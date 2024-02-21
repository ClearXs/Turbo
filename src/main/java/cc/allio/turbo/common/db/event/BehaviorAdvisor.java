package cc.allio.turbo.common.db.event;

import cc.allio.turbo.common.aop.TurboAdvisor;
import cc.allio.turbo.common.aop.TurboAdvisorBuilder;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.function.lambda.MethodPredicate;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Objects;

/**
 * 领域行为切面
 *
 * @author jiangwei
 * @date 2024/1/26 17:13
 * @see BehaviorAdvisorBuilder#builder(Class, MethodPredicate)
 * @since 0.1.0
 */
@Slf4j
public class BehaviorAdvisor extends TurboAdvisor {

    private final DomainEventBus domainEventBus;

    public BehaviorAdvisor(DomainEventBus domainEventBus) {
        super();
        this.domainEventBus = domainEventBus;
    }

    /**
     * @param bean bean
     */
    @Override
    public void preProxyProcess(Object bean) {
        MethodInterceptor methodInterceptor = new BehaviorMethodInterceptor((Subscriber<?>) bean, domainEventBus);
        setAdvice(methodInterceptor);
    }

    @Override
    public void postProxyProcess(Object proxy, Object bean) {
        ((Subscriber<?>) bean).setProxy((Subscriber) proxy);
    }

    public static class BehaviorMethodInterceptor implements MethodInterceptor {

        private final Subscriber<?> bean;
        private final DomainEventBus eventBus;

        public BehaviorMethodInterceptor(Subscriber<?> bean, DomainEventBus eventBus) {
            this.bean = bean;
            this.eventBus = eventBus;
        }

        @Nullable
        @Override
        public Object invoke(@NotNull MethodInvocation invocation) throws Throwable {
            Method method = invocation.getMethod();
            String methodName = method.getName();
            String subscriberName = AopUtils.getTargetClass(bean).getSimpleName();
            switch (methodName) {
                case "getDomainName" -> {
                    return subscriberName;
                }
                case "getEventBus" -> {
                    return eventBus;
                }
                case "onApplicationEvent", "getDomainType" -> {
                    return invocation.proceed();
                }
                case "subscribeOnBefore", "subscribeOn", "subscribeOnInitialize" -> {
                    Object result = invocation.proceed();
                    if (result instanceof Observable<?> observer) {
                        observer.setEventBus(eventBus);
                        observer.setBehavior(method);
                        return observer;
                    }
                    return result;
                }
                default -> {
                    publishOnBehaviorBefore(bean, method, invocation);
                    Object result = invocation.proceed();
                    publishOnBehaviorAfter(bean, method, invocation, result);
                    return result;
                }
            }
        }

        /**
         * 在执行领域行为之前发布领域事件
         *
         * @param bean       领域对象
         * @param method     行为
         * @param invocation aop invocation
         */
        private void publishOnBehaviorBefore(Subscriber<?> bean, Method method, MethodInvocation invocation) {
            String subscriberName = AopUtils.getTargetClass(bean).getSimpleName();
            String methodName = method.getName();
            // DomainBehavior/methodName-before
            String beforePath = subscriberName + StringPool.SLASH + methodName + StringPool.DASH + Subscriber.BEFORE;
            if (eventBus.hasTopic(beforePath)) {
                // 构建事件上下文
                DomainEventContext eventContext = new ThreadLocalWebDomainEventContext(bean, method);
                Object[] arguments = invocation.getArguments();
                MethodParameterMap methodParameterMap = new MethodParameterMap(method, arguments);
                Class<?> domainType = bean.getDomainType();
                // 存放所有参数
                eventContext.putAll(methodParameterMap);
                if (domainType != null) {
                    Object parameterDomain = methodParameterMap.getDomain(domainType);
                    if (parameterDomain != null) {
                        eventContext.putAttribute(DomainEventContext.DOMAIN_KEY, parameterDomain);
                    }
                }
                try {
                    eventBus.publishOnFlux(beforePath, eventContext).subscribe();
                } catch (Throwable ex) {
                    log.warn("behavior event path {} subscribe execute has err", beforePath, ex);
                }
            }
        }

        /**
         * 在执行领域行为之后发布领域事件
         *
         * @param bean       领域对象
         * @param method     行为
         * @param invocation aop invocation
         * @param result     result
         */
        private void publishOnBehaviorAfter(Subscriber<?> bean, Method method, MethodInvocation invocation, Object result) {
            String subscriberName = AopUtils.getTargetClass(bean).getSimpleName();
            String methodName = method.getName();
            // DomainBehavior/methodName
            String path = subscriberName + StringPool.SLASH + methodName;
            // 需要判断是否存在订阅了领域行为，避免多次订阅信息
            if (eventBus.hasTopic(path)) {
                // 构建事件上下文
                DomainEventContext eventContext = new ThreadLocalWebDomainEventContext(bean, method);
                Object[] arguments = invocation.getArguments();
                MethodParameterMap methodParameterMap = new MethodParameterMap(method, arguments);
                Class<?> domainType = bean.getDomainType();
                if (domainType != null) {
                    Object parameterDomain = methodParameterMap.getDomain(domainType);
                    if (result != null && domainType.isAssignableFrom(result.getClass())) {
                        eventContext.putAttribute(DomainEventContext.DOMAIN_KEY, result);
                    } else if (parameterDomain != null) {
                        eventContext.putAttribute(DomainEventContext.DOMAIN_KEY, parameterDomain);
                    }
                }
                if (result != null) {
                    // 放入结果
                    eventContext.putAttribute(DomainEventContext.BEHAVIOR_RESULT_KEY, result);
                }
                // 存放所有参数
                eventContext.putAll(methodParameterMap);
                // 发布领域行为数据
                eventBus.publish(path, eventContext);
            }
        }
    }


    public static class MethodParameterMap extends HashMap<String, Object> {

        public MethodParameterMap(Method method, Object[] arguments) {
            if (arguments != null && arguments.length > 0) {
                Parameter[] parameters = method.getParameters();
                for (int i = 0; i < parameters.length; i++) {
                    Parameter parameter = parameters[i];
                    Object argument = arguments[i];
                    if (argument != null) {
                        put(parameter.getName(), argument);
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
            return values().stream()
                    .filter(Objects::nonNull)
                    .filter(v -> domainType.isAssignableFrom(v.getClass()))
                    .findFirst()
                    .orElse(null);
        }
    }

    /**
     * BehaviorAdvisor 构建
     */
    public static class BehaviorAdvisorBuilder extends TurboAdvisorBuilder<BehaviorAdvisor> {

        protected BehaviorAdvisorBuilder() {
            super(BehaviorAdvisor.class, Subscriber.class::isAssignableFrom);
        }

        /**
         * 获取构造器实例
         *
         * @return TurboAdvisorBuilder
         */
        public static TurboAdvisorBuilder<BehaviorAdvisor> builder(DomainEventBus domainEventBus) {
            BehaviorAdvisorBuilder behaviorAdvisorBuilder = new BehaviorAdvisorBuilder();
            return behaviorAdvisorBuilder.constructParameters(new Object[]{domainEventBus});
        }

    }
}
