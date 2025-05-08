package cc.allio.turbo.common.db.uno.repository;

import cc.allio.turbo.common.db.entity.Entity;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.core.util.concurrent.LockContext;
import cc.allio.uno.core.util.map.OptionalMap;
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
 * @author j.x
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
    public void addIgnoreMethod(String... ignoreMethod) {
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
        // 基于ReentrantLock，实现对Repository方法，进行加锁执行。
        return LockContext.lock(Entity::putThreadLockContext)
                .thenApply(optionContext -> this.doInvoke(invocation, optionContext))
                .lockEnd(k -> Entity.clearThreadLockContext())
                .release()
                .unwrap(Throwable.class);
    }

    /**
     * 执行方法
     *
     * @param invocation  invocation
     * @param lockContext lockContext
     * @return withCall result
     * @throws RuntimeException 当出现异常时抛出
     */
    protected Object doInvoke(MethodInvocation invocation, OptionalMap<String> lockContext) {
        try {
            return invocation.proceed();
        } catch (Throwable e) {
            throw Exceptions.unchecked(e);
        }
    }
}
