package cc.allio.uno.turbo.common.web;

import cc.allio.uno.turbo.common.mybatis.entity.IdEntity;
import cc.allio.uno.turbo.common.mybatis.service.ITurboCrudService;

public abstract class GenericTurboCrudController<T extends IdEntity> extends TurboCrudController<T, ITurboCrudService<T>> {

}
