package cc.allio.turbo.common.db.uno.repository.impl;

import cc.allio.turbo.common.db.entity.Entity;
import cc.allio.turbo.common.db.uno.repository.ITurboCrudRepository;

import java.io.Serializable;

public abstract class TurboCrudRepositoryImpl<T extends Entity, ID extends Serializable> implements ITurboCrudRepository<T, ID> {
}
