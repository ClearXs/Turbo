package cc.allio.turbo.common.db.mybatis.service.impl;

import cc.allio.turbo.common.db.entity.IdEntity;
import cc.allio.turbo.common.db.mybatis.service.ITurboCacheCrudService;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * cache 标识接口
 *
 * @author jiangwei
 * @date 2024/1/24 15:44
 * @since 0.1.0
 */
public abstract class TurboCacheCrudServiceImpl<M extends BaseMapper<T>, T extends IdEntity>
        extends TurboCrudServiceImpl<M, T>
        implements ITurboCacheCrudService<T> {
}
