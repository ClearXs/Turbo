package cc.allio.turbo.modules.development.api.service;

import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.modules.development.api.DomainObject;
import cc.allio.turbo.modules.development.api.GeneralDomainObject;
import cc.allio.turbo.modules.development.api.annotation.Domain;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.core.util.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * {@link IDomainService}注册表。
 * <p>包含所有系统内所有的{@link IDomainService}领域服务的生成，内部还维护了声明式的领域服务的</p>
 * <p>定义了领域切面的实现</p>
 * <p>包括领域服务的scanner</p>
 * <ul>
 *     <li>directly domain service: 该领域服务为抽象的领域对象，不具备任何业务行为</li>
 *     <li>declarative domain service: 该领域服务为声明式的领域对象，具备业务行为，为编码式生成</li>
 * </ul>
 * <p><b>值得注意的是，实现类的所有的实例{@link IDomainService}都是懒加载的，都是会在调用{@link IDomainService#getRepository()}时才会触发加载的逻辑。</b></p>
 * <p><b>即使是声明式的{@link IDomainService}，因为其内部使用的还是directly的实现，其本身只是作为桥接</b></p>
 *
 * <p>操作集</p>
 * <ul>
 *     <li>cover operate: 该操作将会覆盖之前的注册</li>
 *     <li>register operate：注册值</li>
 * </ul>
 *
 * @author j.x
 * @date 2024/2/27 18:41
 * @see DomainServiceScanner
 * @since 0.1.1
 */
public interface DomainServiceRegistry {

    /**
     * 根据boId注册 directly domain service
     *
     * <p>cover operate</p>
     * <p>register operate</p>
     *
     * @param boKey boKey
     * @return directly domain service and it's lazy domain service
     * @throws BizException has error if process on register domain service
     */
    IDomainService<GeneralDomainObject> registerDirectly(String boKey) throws BizException;

    /**
     * @see #registerDeclarative(String, Class)
     * <p>cover operate</p>
     * <p>register operate</p>
     */
    default <T extends DomainObject> IDomainService<T> registerDeclarative(Class<T> domainObjectClass) throws BizException {
        String boKey = getDomainObjectKey(domainObjectClass);
        return registerDeclarative(boKey, domainObjectClass);
    }

    /**
     * 注册declarative domain service，该方法内部会查找缓存中获取，如果获取空则先进行注册。
     *
     * <p>cover operate</p>
     * <p>register operate</p>
     *
     * @param boKey boKey
     * @return {@link LazyDomainService} domain service
     * @throws BizException when domain object service is null or withCall #getBoId
     */
    <T extends DomainObject> IDomainService<T> registerDeclarative(String boKey, Class<T> domainObjectClass) throws BizException;

    /**
     * 注册declarative domain service，该方法内部会查找缓存中获取，如果获取空则先进行注册。
     *
     * <p>cover operate</p>
     * <p>register operate</p>
     *
     * @param domainObjectClass  domainObjectClass
     * @param domainServiceClass domainServiceClass
     * @param <T>                领域对象类型
     * @return IDomainService
     * @throws BizException when domain object service is null or withCall #getBoId
     */
    <T extends DomainObject, S extends IDomainService<T>> S registerDeclarative(Class<T> domainObjectClass, Class<S> domainServiceClass) throws BizException;

    /**
     * 注册declarative domain service
     *
     * <p>cover operate</p>
     * <p>register operate</p>
     *
     * @param generalDomainService generalDomainService not empty
     * @param domainObjectClass    domainObjectClass not empty
     * @param domainServiceClass   domainServiceClass
     * @param <T>                  DomainObject类型
     * @return IDomainService
     * @throws BizException when general domain service is null or domain object service is null
     */
    <T extends DomainObject, S extends IDomainService<T>> S registerDeclarative(IDomainService<GeneralDomainObject> generalDomainService,
                                                                                                Class<T> domainObjectClass,
                                                                                                Class<S> domainServiceClass) throws BizException;

    /**
     * @see #getDomainService(String, Class, Class)
     *
     * <p>register opearte</p>
     */
    IDomainService<GeneralDomainObject> getDomainService(String boKey) throws BizException;

    /**
     * @see #getDomainService(String, Class, Class)
     *
     * <p>register opearte</p>
     */
    <T extends DomainObject> IDomainService<T> getDomainService(Class<T> domainObjectClass) throws BizException;

    /**
     * {@link #getDomainService(String, Class, Class)}
     *
     * <p>register operate</p>
     */
    <T extends DomainObject, S extends IDomainService<T>> S getDomainService(Class<T> domainObjectClass, Class<S> doaminServiceClass) throws BizException;

    /**
     * 给定BoId与DomainObjectClass获取{@link IDomainService}，如果不存在则会将会创建新的IDomainService<GeneralDomainObject>或者创建声明式的{@link IDomainService}
     * <p>该方法会先尝试从缓存中获取声明式的{@link IDomainService}实例，然后才会获取直接式的{@link IDomainService}</p>
     *
     * <p>register operate</p>
     *
     * @param boKey              boKey not empty
     * @param domainObjectClass  domainObjectClass not empty
     * @param domainServiceClass domainServiceClass
     * @param <T>                domain object service
     * @param <S>                sub type {@link IDomainService}
     * @return domain service instance
     * @throws BizException when bo id or domain object class is empty or create domain service failed
     */
    <T extends DomainObject, S extends IDomainService<T>> S getDomainService(String boKey, Class<T> domainObjectClass, Class<S> domainServiceClass) throws BizException;

    /**
     * 根据bo移除缓存内的数据
     *
     * @param boKey boKey
     */
    void remove(String boKey);

    /**
     * 根据domainObjectClass移除缓存内的数据
     *
     * @param domainObjectClass domainObjectClass
     * @param <T>               domain object type
     */
    <T extends DomainObject> void remove(Class<T> domainObjectClass);

    /**
     * return registry registered count
     *
     * @return registered count
     */
    int size();

    /**
     * 基于{@link DomainObject}获取BoKey，需要求加上{@link cc.allio.turbo.modules.development.api.annotation.Domain}注解
     *
     * @param domainObjectClass domainObjectClass
     * @param <T>               domainObjectClass type
     * @return bo key
     * @throws NullPointerException 如果为null
     */
    default <T extends DomainObject> String getDomainObjectKey(Class<T> domainObjectClass) {
        String boKey = DomainObject.getKey(domainObjectClass);
        if (StringUtils.isBlank(boKey)) {
            throw Exceptions.unNull("Domain key is empty");
        }
        return boKey;
    }

    /**
     * 从{@link IDomainService}上带有{@link Domain}注解获取 bo key
     *
     * @param domainServiceClass domainServiceClass
     * @param <T>                DomainObject类型
     * @param <S>                DomainService 类型
     * @return bo key
     * @throws NullPointerException 如果bo key为null
     */
    default <T extends DomainObject, S extends IDomainService<T>> String getDomainServiceKey(Class<S> domainServiceClass) {
        Domain domain = AnnotationUtils.findAnnotation(domainServiceClass, Domain.class);
        if (domain == null) {
            throw Exceptions.unNull(String.format("@Domain annotation non exist %s", domainServiceClass.getName()));
        }
        String boKey = domain.value();
        if (StringUtils.isBlank(boKey)) {
            throw Exceptions.unNull("Domain key is empty");
        }
        return boKey;
    }
}
