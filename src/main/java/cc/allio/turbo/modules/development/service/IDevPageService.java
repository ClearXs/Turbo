package cc.allio.turbo.modules.development.service;

import cc.allio.turbo.common.db.uno.repository.ITurboCrudRepositoryService;
import cc.allio.turbo.modules.development.entity.DevPage;

public interface IDevPageService extends ITurboCrudRepositoryService<DevPage> {

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
