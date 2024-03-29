package cc.allio.turbo.modules.developer.service.impl;

import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.modules.developer.api.GeneralDomainObject;
import cc.allio.turbo.modules.developer.api.service.DomainServiceRegistry;
import cc.allio.turbo.modules.developer.api.service.DomainServiceRegistryImpl;
import cc.allio.turbo.modules.developer.api.service.IDomainService;
import cc.allio.turbo.modules.developer.domain.BoSchema;
import cc.allio.turbo.modules.developer.service.IBoDomainService;
import cc.allio.turbo.modules.developer.service.IDevBoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 业务对象的领域行为的实现。
 * <p>该实现基于{@link cc.allio.uno.core.concurrent.LockContext}</p>
 * <p>在每一个与</p>
 */
@Slf4j
@Service
public class BoDomainServiceImpl implements IBoDomainService {

    private final DomainServiceRegistry domainServiceRegistry;
    private final IDevBoService devBoService;

    public BoDomainServiceImpl(DomainServiceRegistry domainServiceRegistry, IDevBoService devBoService) {
        this.domainServiceRegistry = domainServiceRegistry;
        this.devBoService = devBoService;
    }

    @Override
    public IDomainService<GeneralDomainObject> getBoRepositoryOrThrow(Long boId) throws BizException {
        BoSchema boSchema = devBoService.cacheToSchema(boId);
        if (boSchema != null) {
            return domainServiceRegistry.getDomainService(boSchema.getCode());
        }
        return null;
    }
}
