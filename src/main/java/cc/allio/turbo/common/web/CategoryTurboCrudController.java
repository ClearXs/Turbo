package cc.allio.turbo.common.web;

import cc.allio.turbo.common.db.entity.CategoryEntity;
import cc.allio.turbo.common.db.entity.Entity;

/**
 * 标识类
 *
 * @author j.x
 * @date 2024/1/14 12:37
 * @since 0.1.0
 */
public abstract class CategoryTurboCrudController<T extends Entity & CategoryEntity> extends GenericTurboCrudController<T> {

}
