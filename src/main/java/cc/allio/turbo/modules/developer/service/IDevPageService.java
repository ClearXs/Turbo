package cc.allio.turbo.modules.developer.service;

import cc.allio.turbo.common.db.mybatis.service.ITurboCrudService;
import cc.allio.turbo.modules.developer.entity.DevPage;

public interface IDevPageService extends ITurboCrudService<DevPage> {

    /**
     * 页面发布
     * <ul>
     *     <li>创建的新的菜单项</li>
     *     <li>创建基于/domain/{pageId}的路由</li>
     *     <li>如果先前已经发布过菜单，则删除旧的菜单项</li>
     * </ul>
     *
     * @param devPage devPage
     * @param menuId  menuId
     * @return true if deployment
     */
    boolean deploy(DevPage devPage, Long menuId);
}
