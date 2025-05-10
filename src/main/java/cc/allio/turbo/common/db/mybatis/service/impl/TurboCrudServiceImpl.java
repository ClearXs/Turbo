package cc.allio.turbo.common.db.mybatis.service.impl;

import cc.allio.turbo.common.db.entity.Entity;
import cc.allio.turbo.common.db.mybatis.service.ITurboCrudService;
import cc.allio.turbo.common.domain.DomainEventContext;
import cc.allio.uno.core.bus.EventBus;
import cc.allio.uno.core.reflect.ReflectTools;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.Serializable;

public abstract class TurboCrudServiceImpl<M extends BaseMapper<T>, T extends Entity>
        extends ServiceImpl<M, T>
        implements ITurboCrudService<T> {

    private EventBus<DomainEventContext> eventBus;

    @Override
    public <V extends T> V details(Serializable id) {
        return (V) getById(id);
    }

    @Override
    public Class<T> getDomainType() {
        Class<?> domainType = ReflectTools.getGenericType(this, ServiceImpl.class, 1);
        if (domainType == null) {
            return null;
        } else {
            return (Class<T>) domainType;
        }
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
