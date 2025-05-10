package cc.allio.turbo.modules.development.api.service;

import cc.allio.turbo.common.db.uno.repository.LockRepositoryMethodInterceptor;
import cc.allio.turbo.modules.development.api.DomainObject;
import cc.allio.turbo.modules.development.domain.BoSchema;
import cc.allio.turbo.modules.development.entity.DomainEntity;
import cc.allio.uno.core.function.lambda.ThrowingMethodConsumer;
import cc.allio.uno.core.util.ObjectUtils;
import cc.allio.uno.core.util.map.OptionalMap;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.util.Queue;
import java.util.function.Supplier;

/**
 * 领域对象切面的实现
 *
 * @author j.x
 * @date 2024/2/28 19:48
 * @since 0.1.1
 */
@Slf4j
public class DomainLockRepositoryMethodInterceptor extends LockRepositoryMethodInterceptor {

    private final Supplier<BoSchema> schemaLoader;

    public DomainLockRepositoryMethodInterceptor(Supplier<BoSchema> schemaLoader, String... ignoreMethods) {
        this.schemaLoader = schemaLoader;
        addIgnoreMethod("aspectOn");
        addIgnoreMethod("getDomainAspects");
        if (ObjectUtils.isNotEmpty(ignoreMethods)) {
            for (String ignoreMethod : ignoreMethods) {
                addIgnoreMethod(ignoreMethod);
            }
        }
    }

    @Override
    protected Object doInvoke(MethodInvocation invocation, OptionalMap<String> lockContext) {
        BoSchema schema = schemaLoader.get();
        lockContext.put(DomainEntity.SCHEMA, schema);
        // 领域切面实现
        Object that = invocation.getThis();
        Method method = invocation.getMethod();
        if (that instanceof IDomainService<? extends DomainObject> domainCrudTreeRepositoryService) {
            Queue<DomainAspect> domainAspects = domainCrudTreeRepositoryService.getDomainAspects();
            DomainAspect domainInspect = domainAspects.poll();
            if (domainInspect != null && isMatch(domainInspect.getDomainMethod(), method)) {
                if (log.isDebugEnabled()) {
                    log.debug("domain object [{}] aspect domain method [{}]", schema.getName(), domainInspect.getDomainMethod());
                }
                // save in arguments
                Object[] arguments = invocation.getArguments();
                OptionalMap<String> immutableContext = OptionalMap.immutable(lockContext, arguments);
                ThrowingMethodConsumer<OptionalMap<String>> callback = domainInspect.getCallback();
                try {
                    callback.accept(immutableContext);
                } catch (Throwable ex) {
                    // ignore
                    log.warn("domain object [{}] aspect domain method {} encounter error, now just log error and ignore", schema.getName(), domainInspect.getDomainMethod(), ex);
                }
            }
        }
        return super.doInvoke(invocation, lockContext);
    }

    /**
     * 匹配给定的领域行为是否与领域方法相匹配
     *
     * @param domainBehavior domainBehavior
     * @param method         领域方法
     * @return true if match
     */
    boolean isMatch(String domainBehavior, Method method) {
        return method.getName().equals(domainBehavior);
    }
}
