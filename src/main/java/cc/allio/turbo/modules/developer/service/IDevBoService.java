package cc.allio.turbo.modules.developer.service;

import cc.allio.turbo.common.db.mybatis.service.ITurboCacheCrudService;
import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.modules.developer.domain.BoSchema;
import cc.allio.turbo.modules.developer.entity.DevBo;

public interface IDevBoService extends ITurboCacheCrudService<DevBo> {

    /**
     * 检查给定的BoId是否存在
     *
     * @param boId boId
     * @return true if exist
     */
    Boolean check(Long boId);

    /**
     * bo数据物化
     *
     * @param boId bo id
     * @return if true success
     */
    Boolean materialize(Long boId) throws BizException;

    /**
     * 根据BoId获取BoSchema
     *
     * @param boId boId
     * @return BoSchema
     */
    BoSchema cacheToSchema(Long boId) throws BizException;

    /**
     * 保存BoSchema至缓存中
     * <p><b>增量式更新</b></p>
     * <p>TODO：如果缓存中存在，则两者比较进行增量式更新</p>
     *
     * @param boSchema boSchema
     * @return true if success
     */
    Boolean saveBoSchema(BoSchema boSchema);
}
