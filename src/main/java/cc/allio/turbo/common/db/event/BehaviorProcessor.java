package cc.allio.turbo.common.db.event;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.ClassUtils;
import lombok.AllArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Objects;

/**
 * 创建订阅事件后置处理器
 *
 * @author jiangwei
 * @date 2024/1/26 17:13
 * @since 0.1.0
 */
@AllArgsConstructor
public class BehaviorProcessor implements BeanPostProcessor {

    private final DomainEventBus domainEventBus;

    @Override
    public Object postProcessAfterInitialization(Object bean, @NotNull String beanName) throws BeansException {
        if (!ClassUtils.isAssignable(Subscriber.class, bean.getClass())) {
            return bean;
        }
        AspectJProxyFactory proxyFactory = new AspectJProxyFactory();
        proxyFactory.setTarget(bean);
        proxyFactory.addAspect(BehaviorEventAspect.class);
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("*");
        MethodInterceptor methodInterceptor = new BehaviorMethodInterceptor((Subscriber<?>) bean, domainEventBus);
        proxyFactory.addAdvisor(new DefaultPointcutAdvisor(pointcut, methodInterceptor));
        Object proxy = proxyFactory.getProxy();
        ((Subscriber<?>) bean).setProxy((Subscriber) proxy);
        return proxy;
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
                case "subscribeOn", "subscribeOnInitialize" -> {
                    Object result = invocation.proceed();
                    if (result instanceof Observable<?> observer) {
                        observer.setEventBus(eventBus);
                        observer.setBehavior(method);
                        return observer;
                    }
                    return result;
                }
                default -> {
                    Object result = invocation.proceed();
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
                    return result;
                }
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
}
