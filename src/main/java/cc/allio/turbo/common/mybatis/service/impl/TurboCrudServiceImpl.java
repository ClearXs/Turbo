package cc.allio.turbo.common.mybatis.service.impl;

import cc.allio.turbo.common.mybatis.entity.IdEntity;
import cc.allio.turbo.common.mybatis.service.ITurboCrudService;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

public abstract class TurboCrudServiceImpl<M extends BaseMapper<T>, T extends IdEntity>
        extends ServiceImpl<M, T>
        implements ITurboCrudService<T> {

    @Override
    public <V extends T> V details(Long id) {
        return (V) getById(id);
    }
}
