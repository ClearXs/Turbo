package cc.allio.turbo.common.aop;

import cc.allio.turbo.common.getter.Getter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * via aop advisor interceptor all getter interface, and supported it's all acquiring types.
 *
 * @author jiangwei
 * @date 2024/3/6 20:04
 * @see Getter
 * @since 0.1.1
 */
public class GetterAdvisor extends TurboAdvisor {

    private final Map<Class<?>, Object> getterContainers;
    private final List<Class<? extends Getter>> getterTypes;

    public GetterAdvisor() {
        super();
        setAdvice(new GetterInterceptor(this));
        this.getterContainers = Maps.newConcurrentMap();
        this.getterTypes = Lists.newCopyOnWriteArrayList();
    }

    /**
     * to getter containers register getter type
     *
     * @param getterType the getter type
     * @param target     the getter type target
     * @param <T>        getter type
     */
    public <T> void registerGetterMapping(Class<? extends Getter> getterType, Class<T> supportedType, T target) {
        if (target != null) {
            getterTypes.add(getterType);
            getterContainers.put(supportedType, target);
        }
    }

    /**
     * to getter containers register getter type
     *
     * @param getterType the getter type
     * @param target     lazy target value
     * @param <T>        getter type
     */
    public <T> void registerGetterMappingLazy(Class<? extends Getter> getterType, Class<T> supportedType, Supplier<T> target) {
        if (target != null) {
            getterTypes.add(getterType);
            getterContainers.put(supportedType, target);
        }
    }

    /**
     * to acquire getter object
     *
     * @param getterType the getter type
     * @return the getter object
     */
    Object acquireGetterObject(Class<?> getterType) {
        // traversing getter type
        Set<Class<?>> classes = getterContainers.keySet();
        for (Class<?> inter : classes) {
            if (inter.isAssignableFrom(getterType)) {
                return getterContainers.get(inter);
            }
        }
        return null;
    }

    /**
     * determine supported getter type
     *
     * @param getterType the getterType
     * @return true if supported
     */
    boolean supportGetterType(Class<? extends Getter> getterType) {
        for (Class<? extends Getter> inner : getterTypes) {
            if (inner.isAssignableFrom(getterType)) {
                return true;
            }
        }
        return false;
    }

    static class GetterInterceptor implements MethodInterceptor {

        private final GetterAdvisor advisor;

        public GetterInterceptor(GetterAdvisor advisor) {
            this.advisor = advisor;
        }

        @Nullable
        @Override
        public Object invoke(@NotNull MethodInvocation invocation) throws Throwable {
            Object target = invocation.getThis();
            Method method = invocation.getMethod();
            if (target instanceof Getter getter && (advisor.supportGetterType(getter.getClass()))) {
                Class<?> returnType = method.getReturnType();
                Object value = advisor.acquireGetterObject(returnType);
                if (value instanceof Supplier<?> supplier) {
                    return supplier.get();
                }
                if (value != null) {
                    return value;
                }
            }
            return invocation.proceed();
        }
    }
}
