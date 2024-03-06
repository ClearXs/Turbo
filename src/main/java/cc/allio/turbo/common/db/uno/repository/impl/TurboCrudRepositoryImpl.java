package cc.allio.turbo.common.db.uno.repository.impl;

import cc.allio.turbo.common.db.entity.Entity;
import cc.allio.turbo.common.db.uno.repository.ITurboCrudRepository;

/**
 * base on abstract class {@link ITurboCrudRepository}
 *
 * @author jiangwei
 * @date 2024/2/29 23:31
 * @since 0.1.1
 */
public abstract class TurboCrudRepositoryImpl<T extends Entity> implements ITurboCrudRepository<T> {
}
