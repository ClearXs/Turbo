package cc.allio.turbo.common.web;

import cc.allio.turbo.common.db.entity.TreeEntity;
import cc.allio.turbo.common.db.mybatis.service.ITurboTreeCrudService;
import cc.allio.turbo.common.domain.TreeDomain;

/**
 * 标识通用树查询抽象类
 *
 * @author j.x
 * @date 2024/1/23 20:50
 * @since 0.1.0
 */
public abstract class TurboTreeCrudController<T extends TreeEntity, Z extends TreeDomain<T, Z>>
        extends TurboServiceTreeCrudController<T, Z, ITurboTreeCrudService<T>> {

}
