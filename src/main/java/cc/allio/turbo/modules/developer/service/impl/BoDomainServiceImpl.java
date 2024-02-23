package cc.allio.turbo.modules.developer.service.impl;

import cc.allio.turbo.common.aop.Aspects;
import cc.allio.turbo.common.db.uno.repository.ITurboCrudTreeRepositoryService;
import cc.allio.turbo.common.db.uno.repository.LockRepositoryAdvisor;
import cc.allio.turbo.common.db.uno.repository.LockRepositoryMethodInterceptor;
import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudTreeRepositoryServiceImpl;
import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.common.i18n.DevCodes;
import cc.allio.turbo.modules.developer.domain.BoSchema;
import cc.allio.turbo.modules.developer.entity.DomainEntity;
import cc.allio.turbo.modules.developer.service.IBoDomainService;
import cc.allio.turbo.modules.developer.service.IDevBoService;
import cc.allio.turbo.modules.developer.service.IDevDataSourceService;
import cc.allio.uno.core.api.OptionalContext;
import cc.allio.uno.core.function.lambda.ThrowingMethodConsumer;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 业务对象的领域行为的实现。
 * <p>该实现基于{@link cc.allio.uno.core.concurrent.LockContext}</p>
 * <p>在每一个与</p>
 */
@Slf4j
@Service
public class BoDomainServiceImpl implements IBoDomainService {

    private final Lock lock = new ReentrantLock();
    private final Map<Long, IDomainCrudTreeRepositoryService> boRepositories = Maps.newConcurrentMap();
    private final IDevBoService boService;
    private final IDevDataSourceService dataSourceService;

    public BoDomainServiceImpl(IDevBoService boService, IDevDataSourceService dataSourceService) {
        this.boService = boService;
        this.dataSourceService = dataSourceService;
    }

    @Override
    public void doOnSubscribe() throws Throwable {
        // 订阅boSchema改变时同步更改aop repository，避免数据不一致
        boService.subscribeOn(boService::saveBoSchema)
                .observe(subscription ->
                        // bo schema更新同步更新boRepository
                        subscription.getParameter("boSchema", BoSchema.class)
                                .ifPresent(boSchema -> {
                                    Long dataSourceId = boSchema.getDataSourceId();
                                    CommandExecutor commandExecutor = dataSourceService.getCommandExecutor(dataSourceId);
                                    if (commandExecutor != null) {
                                        lock.lock();
                                        try {
                                            boRepositories.put(Long.valueOf(boSchema.getId()), createAopRepository(commandExecutor, boSchema));
                                        } finally {
                                            lock.unlock();
                                        }
                                    }
                                }));
    }

    /**
     * 基于获取业务对象Repository
     *
     * @param boId boId
     * @return ITurboCrudTreeRepositoryService
     * @throws BizException 如果为null则抛出
     */
    @Override
    public IDomainCrudTreeRepositoryService getBoRepositoryOrThrow(Long boId) throws BizException {
        if (boId == null) {
            throw new BizException(DevCodes.BO_NOT_FOUND);
        }
        lock.lock();
        try {
            IDomainCrudTreeRepositoryService boRepository = boRepositories.get(boId);
            if (boRepository == null) {
                // 尝试从cache中获取bo描述对象
                BoSchema boSchema;
                try {
                    boSchema = boService.cacheToSchema(boId);
                } catch (BizException e) {
                    throw new BizException(DevCodes.BO_NOT_FOUND);
                }
                if (Boolean.FALSE.equals(boSchema.getMaterialize())) {
                    throw new BizException(DevCodes.BO_NONE_MATERIALIZED, boSchema.getName());
                }
                Long dataSourceId = boSchema.getDataSourceId();
                CommandExecutor commandExecutor = dataSourceService.getCommandExecutor(dataSourceId);
                if (commandExecutor != null) {
                    boRepository = createAopRepository(commandExecutor, boSchema);
                    boRepositories.put(boId, boRepository);
                }
            }
            if (boRepository == null) {
                throw new BizException(DevCodes.BO_NOT_FOUND);
            }
            return boRepository;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 基于Aop创建{@link  ITurboCrudTreeRepositoryService}，该Aop将会拦截所有领域方法。
     * <p>每一个领域方法将会附加上基于{@link cc.allio.uno.core.concurrent.LockContext}锁的领域动作</p>
     * <p>LockContext将会执行的动作有</p>
     * <ul>
     *     <li>{@link cc.allio.turbo.modules.developer.entity.DomainEntityTableResolver}的表名解析</li>
     *     <li>{@link cc.allio.turbo.modules.developer.entity.DomainEntityColumnDefListResolver}的列名解析</li>
     * </ul>
     *
     * @param commandExecutor commandExecutor
     * @param schema          schema
     * @return ITurboCrudTreeRepositoryService instance
     */
    IDomainCrudTreeRepositoryService createAopRepository(CommandExecutor commandExecutor, BoSchema schema) {
        BoDomainCrudTreeRepositoryServiceImpl repository = new BoDomainCrudTreeRepositoryServiceImpl(commandExecutor);
        LockRepositoryAdvisor advisor = new LockRepositoryAdvisor(new BoDomainLockRepositoryMethodInterceptor(schema));
        return Aspects.create(repository, advisor);
    }


    public static class BoDomainCrudTreeRepositoryServiceImpl extends SimpleTurboCrudTreeRepositoryServiceImpl<DomainEntity> implements IDomainCrudTreeRepositoryService {

        private final Queue<DomainAspect> domainAspects;

        public BoDomainCrudTreeRepositoryServiceImpl(CommandExecutor commandExecutor) {
            super(commandExecutor, DomainEntity.class);
            this.domainAspects = Queues.newConcurrentLinkedQueue();
        }

        @Override
        public void aspectOn(String domainMethod, ThrowingMethodConsumer<OptionalContext> callback) {
            this.domainAspects.offer(new DomainInspectImpl(domainMethod, callback));
        }

        @Override
        public Queue<DomainAspect> getDomainAspects() {
            return domainAspects;
        }
    }

    @AllArgsConstructor
    @Getter
    public static class DomainInspectImpl implements DomainAspect {

        private final String domainMethod;
        private final ThrowingMethodConsumer<OptionalContext> callback;
    }

    public static class BoDomainLockRepositoryMethodInterceptor extends LockRepositoryMethodInterceptor {
        private final BoSchema schema;

        public BoDomainLockRepositoryMethodInterceptor(BoSchema schema) {
            this.schema = schema;
            addIgnoreMethod("aspectOn");
            addIgnoreMethod("getDomainAspects");
        }

        @Override
        protected Object doInvoke(MethodInvocation invocation, OptionalContext lockContext) {
            lockContext.putAttribute(DomainEntity.SCHEMA, schema);
            // 领域切面实现
            Object theThis = invocation.getThis();
            Method method = invocation.getMethod();
            if (theThis instanceof IDomainCrudTreeRepositoryService domainCrudTreeRepositoryService) {
                Queue<DomainAspect> domainAspects = domainCrudTreeRepositoryService.getDomainAspects();
                DomainAspect domainInspect = domainAspects.poll();
                if (domainInspect != null && isMatch(domainInspect.getDomainMethod(), method)) {
                    if (log.isDebugEnabled()) {
                        log.debug("domain object [{}] aspect domain method [{}]", schema.getName(), domainInspect.getDomainMethod());
                    }
                    // 存入参数
                    Object[] arguments = invocation.getArguments();
                    OptionalContext.ImmutableOptionalContext immutableContext = OptionalContext.immutable(lockContext, arguments);
                    ThrowingMethodConsumer<OptionalContext> callback = domainInspect.getCallback();
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

        boolean isMatch(String domainMethod, Method method) {
            return method.getName().equals(domainMethod);
        }
    }
}
