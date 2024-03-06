package cc.allio.turbo.common.db.mybatis.service.impl;

import cc.allio.turbo.common.db.entity.IdEntity;
import cc.allio.turbo.common.db.event.Subscriber;
import cc.allio.turbo.common.db.mybatis.service.ITurboCrudService;
import cc.allio.uno.core.util.ReflectTools;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.Serializable;

public abstract class TurboCrudServiceImpl<M extends BaseMapper<T>, T extends IdEntity>
        extends ServiceImpl<M, T>
        implements ITurboCrudService<T> {

    private Subscriber<T> proxySubscriber;

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
    public void setProxy(Subscriber<T> subscriber) {
        this.proxySubscriber = subscriber;
    }

    @Override
    public Subscriber<T> getProxy() {
        return proxySubscriber;
    }
}
