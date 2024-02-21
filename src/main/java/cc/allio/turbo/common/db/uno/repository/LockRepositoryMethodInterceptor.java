package cc.allio.turbo.common.db.uno.repository;

import cc.allio.turbo.common.db.entity.Entity;
import cc.allio.uno.core.api.OptionalContext;
import cc.allio.uno.core.concurrent.LockContext;
import cc.allio.uno.core.exception.Exceptions;
import com.google.common.collect.Lists;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.List;

/**
 * {@link LockRepositoryAdvisor}的实现
 *
 * @author jiangwei
 * @date 2024/2/7 14:07
 * @since 0.1.0
 */
public class LockRepositoryMethodInterceptor implements MethodInterceptor {

    // 主要忽略Subscriber的方法
    private final List<String> ignoreMethods = Lists.newArrayList();

    public LockRepositoryMethodInterceptor() {
        ignoreMethods.add("subscribeOnBefore");
        ignoreMethods.add("subscribeOn");
        ignoreMethods.add("subscribeOnInitialize");
        ignoreMethods.add("getDomainName");
        ignoreMethods.add("getDomainType");
        ignoreMethods.add("setProxy");
        ignoreMethods.add("getProxy");
        ignoreMethods.add("onApplicationEvent");
        ignoreMethods.add("doOnSubscribe");
    }

    /**
     * 添加忽略的方法
     */
    protected void addIgnoreMethod(String... ignoreMethod) {
        ignoreMethods.addAll(Lists.newArrayList(ignoreMethod));
    }


    @Nullable
    @Override
    public Object invoke(@NotNull MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        String name = method.getName();
        if (ignoreMethods.contains(name)) {
            return invocation.proceed();
        }
        return LockContext.lock(Entity::putThreadLockContext)
                .lockEnd(lockContext -> Entity.clearThreadLockContext())
                .lockReturn(optionalContext -> doInvoke(invocation, optionalContext));
    }

    /**
     * 执行方法
     *
     * @param invocation  invocation
     * @param lockContext lockContext
     * @return invoke result
     * @throws RuntimeException 当出现异常时抛出
     */
    protected Object doInvoke(MethodInvocation invocation, OptionalContext lockContext) {
        try {
            return invocation.proceed();
        } catch (Throwable e) {
            throw Exceptions.unchecked(e);
        }
    }
}
