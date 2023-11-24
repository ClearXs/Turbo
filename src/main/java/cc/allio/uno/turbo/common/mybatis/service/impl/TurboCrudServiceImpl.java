package cc.allio.uno.turbo.common.mybatis.service.impl;

import cc.allio.uno.turbo.common.mybatis.entity.IdEntity;
import cc.allio.uno.turbo.common.mybatis.service.ITurboCrudService;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

public abstract class TurboCrudServiceImpl<M extends BaseMapper<T>, T extends IdEntity>
        extends ServiceImpl<M, T>
        implements ITurboCrudService<T> {
}
