package cc.allio.turbo.modules.office.service;

import cc.allio.turbo.common.db.mybatis.service.ITurboCrudService;
import cc.allio.turbo.modules.office.entity.Doc;
import cc.allio.turbo.modules.office.entity.DocPermissionGroup;

public interface IDocPermissionGroupService extends ITurboCrudService<DocPermissionGroup> {

    /**
     * 设置文档默认的权限组，该权限组包含两块
     * <ul>
     *     <li>admin</li>
     *     <li>read and write permission</li>
     * </ul>
     *
     * @return ture if success
     */
    Boolean settingDocPermissionGroup(Long docId);

    /**
     * 获取admin权限的权限组
     *
     * @param docId the {@link Doc} id
     * @return {@link DocPermissionGroup} instance or null
     */
    DocPermissionGroup selectAdminPermissionGroup(Long docId);

    /**
     * 获取默认权限的权限组
     *
     * @param docId the {@link Doc} id
     * @return {@link DocPermissionGroup} instance or null
     */
    DocPermissionGroup selectDefaultPermissionGroup(Long docId);
}
