package cc.allio.turbo.modules.developer.api.service;

import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.modules.developer.api.DomainObject;
import cc.allio.turbo.modules.developer.api.GeneralDomainObject;

/**
 * 实现该接口的类可以快速获取领域对象
 *
 * @author j.x
 * @date 2024/2/27 18:25
 * @since 0.1.1
 */
public interface DomainServiceGetter {

    /**
     * 获取{@link IDomainService}实例
     *
     * @param boKey boKey
     * @return IDomainService or null
     */
    default IDomainService<GeneralDomainObject> getDomainService(String boKey) throws BizException {
        return DomainServiceRegistryImpl.obtainDomainService(boKey);
    }

    /**
     * 获取{@link IDomainService}实例
     *
     * @param domainObjectClass domainObjectClass
     * @param <T>               domain object type
     * @return IDomainService or null
     */
    default <T extends DomainObject> IDomainService<T> getDomainService(Class<T> domainObjectClass) throws BizException {
        return DomainServiceRegistryImpl.obtainDomainService(domainObjectClass);
    }

    /**
     * 获取{@link IDomainService}实例
     *
     * @param domainObjectClass  domainObjectClass
     * @param domainServiceClass domainServiceClass
     * @param <T>                domain object type
     * @param <S>                {@link IDomainService} sub type
     * @return IDomainService or null
     */
    default <T extends DomainObject, S extends IDomainService<T>> S getDomainService(Class<T> domainObjectClass, Class<S> domainServiceClass) throws BizException {
        return DomainServiceRegistryImpl.obtainDomainService(domainObjectClass, domainServiceClass);
    }
}
