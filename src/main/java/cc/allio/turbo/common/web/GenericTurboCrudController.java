package cc.allio.turbo.common.web;

import cc.allio.turbo.common.mybatis.entity.IdEntity;
import cc.allio.turbo.common.mybatis.service.ITurboCrudService;

/**
 * 通用crud接口
 *
 * @author j.x
 * @date 2023/11/30 10:59
 * @since 0.1.0
 */
public abstract class GenericTurboCrudController<T extends IdEntity>
        extends TurboCrudController<T, ITurboCrudService<T>, T> {

}
