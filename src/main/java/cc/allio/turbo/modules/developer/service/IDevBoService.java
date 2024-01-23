package cc.allio.turbo.modules.developer.service;

import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.common.db.mybatis.service.ITurboCrudService;
import cc.allio.turbo.modules.developer.domain.BoSchema;
import cc.allio.turbo.modules.developer.entity.DevBo;

public interface IDevBoService extends ITurboCrudService<DevBo> {

    /**
     * 根据BoId获取BoSchema
     *
     * @param boId boId
     * @return BoSchema
     */
    BoSchema toSchema(Long boId) throws BizException;
}
