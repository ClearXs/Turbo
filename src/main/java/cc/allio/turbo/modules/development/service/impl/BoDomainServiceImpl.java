package cc.allio.turbo.modules.development.service.impl;

import cc.allio.turbo.common.domain.DomainEventContext;
import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.modules.development.api.GeneralDomainObject;
import cc.allio.turbo.modules.development.api.service.DomainServiceRegistry;
import cc.allio.turbo.modules.development.api.service.IDomainService;
import cc.allio.turbo.modules.development.domain.BoSchema;
import cc.allio.turbo.modules.development.service.IBoDomainService;
import cc.allio.turbo.modules.development.service.IDevBoService;
import cc.allio.uno.core.bus.EventBus;
import cc.allio.uno.core.util.concurrent.LockContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 业务对象的领域行为的实现。
 * <p>该实现基于{@link LockContext}</p>
 * <p>在每一个与</p>
 */
@Slf4j
@Service
public class BoDomainServiceImpl implements IBoDomainService {

    private final DomainServiceRegistry domainServiceRegistry;
    private final IDevBoService devBoService;
    private EventBus<DomainEventContext> eventBus;

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

    @Override
    public void setDomainEventBus(EventBus<DomainEventContext> eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public EventBus<DomainEventContext> getDomainEventBus() {
        return eventBus;
    }
}
