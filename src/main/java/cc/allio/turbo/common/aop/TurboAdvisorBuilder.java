package cc.allio.turbo.common.aop;

import cc.allio.uno.core.api.Self;
import cc.allio.uno.core.function.lambda.MethodPredicate;
import cc.allio.uno.core.util.ClassUtils;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.core.util.ObjectUtils;
import com.google.common.collect.Lists;
import org.aopalliance.aop.Advice;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * {@link TurboAdvisor}构建器
 *
 * @author jiangwei
 * @date 2024/2/14 10:46
 * @since 0.1.0
 */
public abstract class TurboAdvisorBuilder<T extends TurboAdvisor> implements Self<TurboAdvisorBuilder<T>> {

    private final Class<T> targetClass;
    private final List<String> mappedNames;
    private final MethodPredicate<Class<?>> allowBuilder;
    private Integer order;
    private Advice advice;
    private Object[] parameters;

    protected TurboAdvisorBuilder(Class<T> targetClass, MethodPredicate<Class<?>> allowBuilder) {
        this.targetClass = targetClass;
        this.mappedNames = Lists.newArrayList();
        this.allowBuilder = allowBuilder;
    }

    /**
     * 简单获取构造器实例
     *
     * @param allowBuilder 是否允许{@link #build()}
     * @param <T>          {@link TurboAdvisor}类型
     * @return TurboAdvisorBuilder
     */
    public static <T extends TurboAdvisor> TurboAdvisorBuilder<T> builder(Class<T> advisorClass, MethodPredicate<Class<?>> allowBuilder) {
        return new SimpleTurboAdvisorBuilder<>(advisorClass, allowBuilder);
    }

    /**
     * 切入点正则表达式
     *
     * @param mappedNames mappedNames
     * @return TurboAdvisorBuilder
     */
    public TurboAdvisorBuilder<T> mappedNames(String... mappedNames) {
        this.mappedNames.addAll(Arrays.stream(mappedNames).toList());
        return self();
    }

    /**
     * 多个{@link TurboAdvisor}的执行顺序
     *
     * @param order order
     * @return TurboAdvisorBuilder
     */
    public TurboAdvisorBuilder<T> order(int order) {
        this.order = order;
        return self();
    }

    /**
     * advice 实例
     *
     * @param advice advice
     * @return TurboAdvisorBuilder
     */
    public TurboAdvisorBuilder<T> advice(Advice advice) {
        this.advice = advice;
        return self();
    }

    /**
     * 构造参数
     *
     * @param parameters parameters
     * @return Object
     */
    public TurboAdvisorBuilder<T> constructParameters(Object[] parameters) {
        this.parameters = parameters;
        return self();
    }

    /**
     * 基于{@link #allowBuilder}测试是否允许构建
     *
     * @param proxyClass 用于测试的class对象
     * @return true if build
     */
    public boolean allow(Class<?> proxyClass) {
        return allowBuilder != null && allowBuilder.test(proxyClass);
    }

    /**
     * 构建{@link TurboAdvisor}实例
     *
     * @return advisor instance or null
     */
    public T build() {
        List<Object> params = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(mappedNames)) {
            params.add(mappedNames);
        }
        if (ObjectUtils.isNotEmpty(parameters)) {
            Collections.addAll(params, parameters);
        }
        T advisor = ClassUtils.newInstance(targetClass, params.toArray());
        if (advisor != null) {
            if (advice != null) {
                advisor.setAdvice(advice);
            }
            if (order != null) {
                advisor.setOrder(order);
            }
            return advisor;
        }
        return null;
    }

    public static class SimpleTurboAdvisorBuilder<T extends TurboAdvisor> extends TurboAdvisorBuilder<T> {
        public SimpleTurboAdvisorBuilder(Class<T> advisorClass, MethodPredicate<Class<?>> allowBuilder) {
            super(advisorClass, allowBuilder);
        }
    }
}
