package cc.allio.uno.turbo.common.mybatis.service.impl;

import cc.allio.uno.turbo.common.mybatis.entity.TreeEntity;
import cc.allio.uno.turbo.common.mybatis.service.ITurboTreeCrudService;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * turbo service impl
 *
 * @author j.x
 * @date 2023/11/9 18:29
 * @since 1.0.0
 */
public abstract class TurboTreeCrudServiceImpl<M extends BaseMapper<T>, T extends TreeEntity>
        extends TurboCrudServiceImpl<M, T>
        implements ITurboTreeCrudService<T> {
}
