package cc.allio.turbo.modules.developer.api.service;

import cc.allio.turbo.common.aop.Aspects;
import cc.allio.turbo.common.aop.GetterAdvisor;
import cc.allio.turbo.common.db.event.BehaviorAdvisor;
import cc.allio.turbo.common.db.event.DomainEventBus;
import cc.allio.turbo.common.db.uno.repository.LockRepositoryAdvisor;
import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.common.getter.ApplicationContextGetter;
import cc.allio.turbo.common.i18n.DevCodes;
import cc.allio.turbo.modules.developer.api.DomainObject;
import cc.allio.turbo.modules.developer.api.GeneralDomainObject;
import cc.allio.turbo.modules.developer.domain.BoSchema;
import cc.allio.turbo.modules.developer.service.IDevBoService;
import cc.allio.turbo.modules.developer.service.IDevDataSourceService;
import cc.allio.uno.core.concurrent.LockContext;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.core.function.lambda.ThrowingMethodSupplier;
import cc.allio.uno.core.type.Types;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.data.orm.executor.AggregateCommandExecutor;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.support.GenericApplicationContext;
import reactor.core.Disposable;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

/**
 * {@link DomainServiceRegistry}的默认实现。
 * <p>该实现大量用到Map缓存进行存放{@link IDomainService}实例，<b>值得注意其实例都是使用{@link #createAop(AggregateCommandExecutor, BoSchema, Class)}cglib的代理实例</b></p>
 * <p>此外，该类的所有公有方法都加上锁进行获取。</p>
 * <p>该实现里面通过订阅{@link BoSchema}的变化，能够动态的变化对应的{@link IDomainService}</p>
 *
 * @author j.x
 * @date 2024/3/3 23:48
 * @since 0.1.1
 */
@Slf4j
public final class DomainServiceRegistryImpl implements DomainServiceRegistry, SmartLifecycle, ApplicationContextAware {

    // 该注册表存储着所有领域对象的领域服务
    private final Map<String, IDomainService<GeneralDomainObject>> directlyDomainRegistry;
    // 声明式的domain service 注册表
    private final Map<Class<? extends DomainObject>, IDomainService<? extends DomainObject>> declarativeDomainRegistry;
    // 用于维护领域对象bo key与声明式的DomainService的关系
    private final Map<String, IDomainService<? extends DomainObject>> intrinsicDeclarativeDomainService;
    private final Lock lock;
    private final IDevDataSourceService dataSourceService;
    private final IDevBoService devBoService;
    private GenericApplicationContext applicationContext;
    private final AtomicBoolean isInitialized = new AtomicBoolean(false);
    private final DomainEventBus domainEventBus;
    private Disposable disposable;

    public static DomainServiceRegistry INSTANCE;

    public DomainServiceRegistryImpl(IDevBoService devBoService, IDevDataSourceService dataSourceService, DomainEventBus domainEventBus) {
        this.devBoService = devBoService;
        this.dataSourceService = dataSourceService;
        this.declarativeDomainRegistry = Maps.newConcurrentMap();
        this.directlyDomainRegistry = Maps.newConcurrentMap();
        this.intrinsicDeclarativeDomainService = Maps.newConcurrentMap();
        this.domainEventBus = domainEventBus;
        this.lock = new ReentrantLock();
    }

    @Override
    public void start() {
        // 订阅boSchema改变时同步更改aop repository，避免数据不一致
        this.disposable = devBoService.subscribeOn(devBoService::saveBoSchema)
                .observe(subscription -> {
                            Optional<BoSchema> boSchemaOptional = subscription.getParameter("boSchema", BoSchema.class);
                            if (boSchemaOptional.isEmpty()) {
                                return;
                            }
                            // bo schema更新同步更新boRepository
                            BoSchema boSchema = boSchemaOptional.get();
                            Long dataSourceId = boSchema.getDataSourceId();
                            CommandExecutor commandExecutor = dataSourceService.getCommandExecutor(dataSourceId);
                            if (commandExecutor == null) {
                                return;
                            }
                            String boKey = boSchema.getCode();
                            combinedRegistration(boKey);
                        }
                );

        // domain service scanner and register
        DomainServiceScanner scanner = new DomainServiceScanner(applicationContext);
        Set<DomainBeanDefinition> domainServiceDefinition = scanner.scanCandidate();
        for (DomainBeanDefinition domainBeanDefinition : domainServiceDefinition) {
            beanRegistration(domainBeanDefinition);
        }

        // set registry initial complete
        isInitialized.set(true);
    }

    /**
     * based on {@link DomainBeanDefinition}, create domain service then register application bean factory.
     * <p>throwing exception if creating and registering domain service. bean factory will be detection and throw exception.</p>
     *
     * @param domainBeanDefinition the domainBeanDefinition
     */
    void beanRegistration(DomainBeanDefinition domainBeanDefinition) {
        String domainName = domainBeanDefinition.getDomainName();
        Class<DomainObject> domainObjectClass = (Class<DomainObject>) domainBeanDefinition.getDomainObjectClass();
        Class<IDomainService<DomainObject>> domainServiceClass = (Class<IDomainService<DomainObject>>) domainBeanDefinition.getDomainServiceClass();
        applicationContext.registerBean(
                domainName,
                domainServiceClass,
                () -> {
                    try {
                        return registerDeclarative(domainObjectClass, domainServiceClass);
                    } catch (BizException ex) {
                        throw Exceptions.unchecked(ex);
                    }
                });
    }

    /**
     * 基于缓存信息，合并并覆盖注册
     *
     * @param boKey boKey
     */
    void combinedRegistration(String boKey) {
        LockContext.lock(lock)
                // 直接变更直接式的IDomainService
                .then(() -> registerDirectly(boKey))
                .then(() -> {
                    IDomainService<? extends DomainObject> domainService = intrinsicDeclarativeDomainService.get(boKey);
                    // 声明式的bo对象只能在系统启动时添加，如果此时新增Bo对象，是不会对原先的声明式bo对象进行更新
                    // 但如果更改了bo对象的schema，需要重新注册声明式bo对象，所以加了此判断
                    if (domainService != null) {
                        IDomainService<GeneralDomainObject> generalDomainObjectIDomainService = directlyDomainRegistry.get(boKey);
                        if (generalDomainObjectIDomainService != null) {
                            registerDeclarative(generalDomainObjectIDomainService, domainService.getEntityClass(), domainService.getClass());
                        }
                    }
                })
                .release()
                .unchecked();
    }

    @Override
    public IDomainService<GeneralDomainObject> registerDirectly(String boKey) throws BizException {
        return LockContext.lock(lock)
                .then(() -> {
                    if (StringUtils.isBlank(boKey)) {
                        throw new BizException(DevCodes.BO_NOT_FOUND);
                    }
                })
                .lockReturn(() -> {
                    IDomainService<GeneralDomainObject> domainService = directlyDomainRegistry.get(boKey);
                    if (domainService != null) {
                        return domainService;
                    }
                    if (log.isDebugEnabled()) {
                        log.debug("register directly domain service, the bo key is {}", boKey);
                    }
                    ThrowingMethodSupplier<IDomainService<GeneralDomainObject>> loader =
                            () -> {
                                IDomainService<GeneralDomainObject> internalDomainService = null;
                                // 尝试从cache中获取bo描述对象
                                BoSchema boSchema = devBoService.cacheToSchema(boKey);
                                if (boSchema == null) {
                                    throw new BizException(DevCodes.BO_NOT_FOUND);
                                }
                                if (Boolean.FALSE.equals(boSchema.getMaterialize())) {
                                    throw new BizException(DevCodes.BO_NONE_MATERIALIZED, boSchema.getName());
                                }
                                Long dataSourceId = boSchema.getDataSourceId();
                                AggregateCommandExecutor commandExecutor = dataSourceService.getCommandExecutor(dataSourceId);
                                if (commandExecutor != null) {
                                    internalDomainService = createAop(commandExecutor, boSchema, GeneralDomainObject.class);
                                }
                                if (internalDomainService == null) {
                                    throw new BizException(DevCodes.BO_NOT_FOUND);
                                }
                                return internalDomainService;
                            };
                    var lazyDomainService = new LazyDomainService<>(loader);
                    directlyDomainRegistry.put(boKey, lazyDomainService);
                    return lazyDomainService;
                })
                .unwrap(BizException.class);
    }

    @Override
    public <T extends DomainObject> IDomainService<T> registerDeclarative(String boKey, Class<T> domainObjectClass) throws BizException {
        if (StringUtils.isBlank(boKey) || domainObjectClass == null) {
            throw new BizException(DevCodes.BO_NOT_FOUND);
        }
        IDomainService<GeneralDomainObject> generalDomainService = directlyDomainRegistry.get(boKey);
        if (generalDomainService == null) {
            generalDomainService = registerDirectly(boKey);
        }
        DeclareDomainCrudTreeRepositoryServiceImpl<T> declareDomainRepository = new DeclareDomainCrudTreeRepositoryServiceImpl<>(generalDomainService, domainObjectClass);
        IDomainService<T> aopifyDomainService = aopify(declareDomainRepository, DeclareDomainCrudTreeRepositoryServiceImpl.class);
        return registerDeclarative(boKey, domainObjectClass, aopifyDomainService);
    }

    @Override
    public <T extends DomainObject, S extends IDomainService<T>> S registerDeclarative(Class<T> domainObjectClass,
                                                                                       Class<S> domainServiceClass) throws BizException {
        String boKey = getDomainServiceKey(domainServiceClass);
        IDomainService<GeneralDomainObject> generalDomainService = directlyDomainRegistry.get(boKey);
        if (generalDomainService == null) {
            generalDomainService = registerDirectly(boKey);
        }
        return registerDeclarative(generalDomainService, domainObjectClass, domainServiceClass);
    }

    @Override
    public <T extends DomainObject, S extends IDomainService<T>> S registerDeclarative(IDomainService<GeneralDomainObject> generalDomainService,
                                                                                       Class<T> domainObjectClass,
                                                                                       Class<S> domainServiceClass) throws BizException {
        return LockContext.lock(lock)
                .then(() -> {
                    if (generalDomainService == null || domainObjectClass == null) {
                        throw new BizException(DevCodes.BO_NOT_FOUND);
                    }
                })
                .lockReturn(() -> {
                    S domainService = createDeclarativeDomainService(generalDomainService, domainObjectClass, domainServiceClass);
                    String boKey = getDomainServiceKey(domainServiceClass);
                    return registerDeclarative(boKey, domainObjectClass, domainService);
                })
                .unwrap(BizException.class);
    }

    <T extends DomainObject, S extends IDomainService<T>> S registerDeclarative(String boKey, Class<T> domainObjectClass, S domainService) throws BizException {
        if (domainObjectClass == null || domainService == null) {
            throw new BizException(DevCodes.BO_NOT_FOUND);
        }
        if (log.isDebugEnabled()) {
            log.debug("register declarative domain object, " +
                    "the bo key {} domain object clas is {} domain service is {}", boKey, domainObjectClass.getName(), AopUtils.getTargetClass(domainService).getClass().getName());
        }
        declarativeDomainRegistry.put(domainObjectClass, domainService);
        intrinsicDeclarativeDomainService.put(boKey, domainService);
        return domainService;
    }

    /**
     * 基于domainServiceClass创建，如果它是接口或者非{@link DeclareDomainCrudTreeRepositoryServiceImpl}的继承类，
     * 则返回基于{@link DeclareDomainCrudTreeRepositoryServiceImpl}创建的{@link IDomainService}.
     * <p>如果创建过程中出现问题，则也返回默认基于{@link DeclareDomainCrudTreeRepositoryServiceImpl}创建的{@link IDomainService}.</p>
     *
     * @param generalDomainService generalDomainService
     * @param domainObjectClass    domainObjectClass
     * @param domainServiceClass   domainServiceClass
     * @param <T>                  DomainObject type
     * @param <S>                  IDomainService type
     * @return IDomainService
     * @throws IllegalArgumentException when determine constructor from domain service class is not found or based on constructor create failed
     */
    <T extends DomainObject, S extends IDomainService<T>> S createDeclarativeDomainService(IDomainService<GeneralDomainObject> generalDomainService,
                                                                                           Class<T> domainObjectClass,
                                                                                           Class<S> domainServiceClass) {
        Supplier<S> defaultCreator =
                () -> {
                    DeclareDomainCrudTreeRepositoryServiceImpl<T> declareDomainRepository = new DeclareDomainCrudTreeRepositoryServiceImpl<>(generalDomainService, domainObjectClass);
                    IDomainService<T> aopifyDomainService = aopify(declareDomainRepository, domainServiceClass);
                    return (S) aopifyDomainService;
                };
        // 判断是否为接口或者是非DeclareDomainCrudTreeRepositoryServiceImpl的实现类
        if (domainServiceClass.isInterface() || !DeclareDomainCrudTreeRepositoryServiceImpl.class.isAssignableFrom(domainServiceClass)) {
            return defaultCreator.get();
        }
        Constructor<S>[] candidate = (Constructor<S>[]) domainServiceClass.getDeclaredConstructors();
        Constructor<S> constructorToUse = determineConstructor(candidate);
        if (constructorToUse == null) {
            throw new IllegalArgumentException(
                    String.format("class type %s create domain service impl has err, " +
                            "no constructor found or based on constructor create failed", domainServiceClass.getName()));
        }
        // 取DeclareDomainCrudTreeRepositoryServiceImpl构造方法的两个参数
        List<Object> args = Lists.newArrayList();
        args.add(generalDomainService);
        args.add(domainObjectClass);
        Class<?>[] parameterTypes = constructorToUse.getParameterTypes();
        for (int i = 2; i < parameterTypes.length; i++) {
            Class<?> type = parameterTypes[i];
            if (Types.isBean(type)) {
                Object bean = applicationContext.getBean(type);
                args.add(bean);
            } else {
                args.add(null);
            }
        }
        try {
            S domainServiceImpl = constructorToUse.newInstance(args.toArray(Object[]::new));
            return aopify(domainServiceImpl, domainServiceClass);
        } catch (Throwable ex) {
            throw new IllegalArgumentException(
                    String.format("class type %s create domain service impl has err, " +
                            "now return by DeclareDomainCrudTreeRepositoryServiceImpl creator domain service", domainServiceClass.getName()), ex);
        }
    }

    /**
     * 基于构造操作中含有 IDomainService<GeneralDomainObject>并且构造参数大于2
     *
     * @param constructors constructors
     * @return constructor or null
     */
    <T extends DomainObject, S extends IDomainService<T>> Constructor<S> determineConstructor(Constructor<S>[] constructors) {
        for (Constructor<S> constructor : constructors) {
            int parameterCount = constructor.getParameterCount();
            if (parameterCount < 2) {
                continue;
            }
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            Class<?> type1 = parameterTypes[0];
            if (type1.isAssignableFrom(IDomainService.class)) {
                return constructor;
            }
        }
        return null;
    }

    @Override
    public IDomainService<GeneralDomainObject> getDomainService(String boKey) throws BizException {
        IDomainService<GeneralDomainObject> generalDomainService = directlyDomainRegistry.get(boKey);
        if (generalDomainService == null) {
            return registerDirectly(boKey);
        }
        return generalDomainService;
    }

    @Override
    public <T extends DomainObject> IDomainService<T> getDomainService(Class<T> domainObjectClass) throws BizException {
        if (domainObjectClass == null) {
            throw new BizException(DevCodes.BO_NOT_FOUND);
        }
        IDomainService<? extends DomainObject> domainService = declarativeDomainRegistry.get(domainObjectClass);
        if (domainService == null) {
            String boKey = getDomainObjectKey(domainObjectClass);
            return getDomainService(boKey, domainObjectClass, DeclareDomainCrudTreeRepositoryServiceImpl.class);
        }
        return (IDomainService<T>) domainService;
    }

    @Override
    public <T extends DomainObject, S extends IDomainService<T>> S getDomainService(Class<T> domainObjectClass, Class<S> doaminServiceClass) throws BizException {
        if (domainObjectClass == null || doaminServiceClass == null) {
            throw new BizException(DevCodes.BO_NOT_FOUND);
        }
        IDomainService<? extends DomainObject> domainService = declarativeDomainRegistry.get(domainObjectClass);
        if (domainService == null) {
            String boKey = getDomainServiceKey(doaminServiceClass);
            return getDomainService(boKey, domainObjectClass, doaminServiceClass);
        }
        return (S) domainService;
    }

    @Override
    public <T extends DomainObject, S extends IDomainService<T>> S getDomainService(String boKey, Class<T> domainObjectClass, Class<S> domainServiceClass) throws BizException {
        if (StringUtils.isBlank(boKey)) {
            throw new BizException(DevCodes.BO_NOT_FOUND);
        }
        IDomainService<? extends DomainObject> instance =
                LockContext.lock(lock)
                        .lockReturn(() -> {
                            IDomainService<GeneralDomainObject> internal = directlyDomainRegistry.get(boKey);
                            if (internal == null) {
                                internal = registerDirectly(boKey);
                            }
                            if (GeneralDomainObject.class.isAssignableFrom(domainObjectClass)) {
                                return internal;
                            } else {
                                IDomainService<? extends DomainObject> domainService = declarativeDomainRegistry.get(domainObjectClass);
                                if (domainService == null) {
                                    return registerDeclarative(internal, domainObjectClass, domainServiceClass);
                                } else {
                                    return domainService;
                                }
                            }
                        })
                        .unchecked();
        return (S) instance;
    }

    @Override
    public void remove(String boKey) {
        LockContext.lock(lock)
                .then(() -> directlyDomainRegistry.remove(boKey))
                .then(() -> {
                    IDomainService<? extends DomainObject> domainService = intrinsicDeclarativeDomainService.get(boKey);
                    if (domainService != null) {
                        intrinsicDeclarativeDomainService.remove(boKey);
                        declarativeDomainRegistry.remove(domainService.getEntityClass());
                    }
                })
                .release()
                .unchecked();
    }

    @Override
    public <T extends DomainObject> void remove(Class<T> domainObjectClass) {
        LockContext.lock(lock)
                .then(() -> {
                    String boKey = getDomainObjectKey(domainObjectClass);
                    if (StringUtils.isNotBlank(boKey)) {
                        intrinsicDeclarativeDomainService.remove(boKey);
                    }
                })
                .then(() -> declarativeDomainRegistry.remove(domainObjectClass))
                .release()
                .unchecked();
    }

    @Override
    public int size() {
        return directlyDomainRegistry.size();
    }

    /**
     * 从给定原始的{@link IDomainService}进行赋值，并创建AOP{@link IDomainService}。
     *
     * @param original original class
     * @return ITurboCrudTreeRepositoryService instance
     * @see #createAop(AggregateCommandExecutor, BoSchema, Class)
     */
    <T extends DomainObject> IDomainService<T> copyToAop(IDomainService<?> original, Class<T> domainObjectClass) {
        AggregateCommandExecutor commandExecutor = original.getRepository().getExecutor();
        BoSchema schema = original.getBoSchema();
        return createAop(commandExecutor, schema, domainObjectClass);
    }

    /**
     * 基于{@link CommandExecutor}与{@link BoSchema}创建AOP的{@link IDomainService}
     *
     * @param commandExecutor commandExecutor
     * @param schema          schema
     * @return ITurboCrudTreeRepositoryService instance
     * @see #aopify(IDomainService, Class)
     */
    <T extends DomainObject> IDomainService<T> createAop(AggregateCommandExecutor commandExecutor, BoSchema schema, Class<T> domainObjectClass) {
        DomainCrudTreeRepositoryServiceImpl<T> repository = new DomainCrudTreeRepositoryServiceImpl<>(commandExecutor, schema, domainObjectClass);
        return aopify(repository, DomainCrudTreeRepositoryServiceImpl.class);
    }

    /**
     * <b>使得{@link IDomainService}进行AOP化</b>
     * <p>基于Aop创建{@link IDomainService}，该Aop将会拦截所有领域方法。</p>
     * <p>每一个领域方法将会附加上基于{@link cc.allio.uno.core.concurrent.LockContext}锁的领域动作</p>
     * <p>LockContext将会执行的动作有</p>
     * <ul>
     *     <li>{@link cc.allio.turbo.modules.developer.entity.DomainEntityTableResolver}的表名解析</li>
     *     <li>{@link cc.allio.turbo.modules.developer.entity.DomainEntityColumnDefListResolver}的列名解析</li>
     * </ul>
     *
     * @param original                 非AOP原始的{@link IDomainService}实例
     * @param actualDomainServiceClass actualDomainServiceClass
     * @return ITurboCrudTreeRepositoryService instance
     */
    <T extends DomainObject, S extends IDomainService<T>> S aopify(IDomainService<?> original, Class<S> actualDomainServiceClass) {
        Aspects.AspectJProxyBuilder builder = Aspects.builder().target(original);
        if (actualDomainServiceClass.isInterface()) {
            builder.addInterface(actualDomainServiceClass);
        }
        // 添加 LockRepository Advisor
        DomainLockRepositoryMethodInterceptor domainInterceptor = new DomainLockRepositoryMethodInterceptor(original::getBoSchema);
        domainInterceptor.addIgnoreMethod("getBoSchema");
        domainInterceptor.addIgnoreMethod("getApplicationContext");
        LockRepositoryAdvisor lockRepositoryAdvisor = new LockRepositoryAdvisor(domainInterceptor);
        builder.addAdvisor(lockRepositoryAdvisor);
        // 添加 method behavior advisor
        BehaviorAdvisor behaviorAdvisor = new BehaviorAdvisor(domainEventBus);
        builder.addAdvisor(behaviorAdvisor);
        // add getter advisor
        GetterAdvisor getterAdvisor = new GetterAdvisor();
        getterAdvisor.registerGetterMapping(ApplicationContextGetter.class, ApplicationContext.class, applicationContext);
        getterAdvisor.registerGetterMappingLazy(BoSchemaGetter.class, BoSchema.class, original::getBoSchema);
        builder.addAdvisor(getterAdvisor);
        return builder.proxy();
    }

    @Override
    public void stop() {
        if (disposable != null) {
            disposable.dispose();
        }
        directlyDomainRegistry.clear();
        declarativeDomainRegistry.clear();
        intrinsicDeclarativeDomainService.clear();
    }

    @Override
    public boolean isRunning() {
        return isInitialized.get();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (GenericApplicationContext) applicationContext;
    }


    /**
     * {@link #getDomainService(String)}
     *
     * @param boKey boKey
     * @return bo service instance or null
     */
    public static IDomainService<GeneralDomainObject> obtainDomainService(String boKey) throws BizException {
        if (INSTANCE != null) {
            return INSTANCE.getDomainService(boKey);
        }
        return null;
    }


    /**
     * {@link #getDomainService(Class)}
     *
     * @param domainObjectClass domainObjectClass not empty
     * @param <T>               DomainObject type
     * @return {@link LazyDomainService} instance or null
     * @throws BizException when bo id or domain object class is empty or create domain service failed
     */
    public static <T extends DomainObject> IDomainService<T> obtainDomainService(Class<T> domainObjectClass) throws BizException {
        if (INSTANCE != null) {
            return INSTANCE.getDomainService(domainObjectClass);
        }
        return null;
    }

    /**
     * {@link #getDomainService(Class, Class)}
     *
     * @param domainObjectClass domainObjectClass not empty
     * @param <T>               DomainObject type
     * @param <S>               {@link IDomainService} sub type
     * @return {@link LazyDomainService} instance or null
     * @throws BizException when bo id or domain object class is empty or create domain service failed
     */
    public static <T extends DomainObject, S extends IDomainService<T>> S obtainDomainService(Class<T> domainObjectClass, Class<S> domainServiceClass) throws BizException {
        if (INSTANCE != null) {
            return INSTANCE.getDomainService(domainObjectClass, domainServiceClass);
        }
        return null;
    }


    /**
     * {@link #getDomainService(String, Class, Class)}
     *
     * @param boKey             boKey not empty
     * @param domainObjectClass domainObjectClass not empty
     * @param <T>               domain object service
     * @return {@link LazyDomainService} instance or null
     * @throws BizException when bo id or domain object class is empty or create domain service failed
     */
    public static <T extends DomainObject> IDomainService<T> obtainDomainService(String boKey, Class<T> domainObjectClass) throws BizException {
        if (INSTANCE != null) {
            return INSTANCE.getDomainService(boKey, domainObjectClass, DeclareDomainCrudTreeRepositoryServiceImpl.class);
        }
        return null;
    }
}