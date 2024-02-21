package cc.allio.turbo.modules.developer.service;

import cc.allio.turbo.common.db.mybatis.service.ITurboCrudService;
import cc.allio.turbo.modules.developer.entity.DevForm;

public interface IDevFormService extends ITurboCrudService<DevForm> {

    /**
     * 表单数据发布：
     * <ul>
     *     <li>更改表单相关schema数据</li>
     *     <li>更改bo数据</li>
     *     <li>表单schema数据放入缓存</li>
     * </ul>
     */
    boolean publish(DevForm form);
}
