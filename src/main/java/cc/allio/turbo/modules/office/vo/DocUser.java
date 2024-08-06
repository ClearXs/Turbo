package cc.allio.turbo.modules.office.vo;

import cc.allio.turbo.modules.office.entity.Doc;
import cc.allio.turbo.modules.office.entity.DocCustomization;
import cc.allio.turbo.modules.office.entity.DocPermissionGroup;
import lombok.Data;

/**
 * 文档用户，包含
 * <ul>
 *     <li>用户基本信息</li>
 *     <li>文档权限信息</li>
 * </ul>
 *
 * @author j.x
 * @date 2024/5/9 18:40
 * @since 0.0.1
 */
@Data
public class DocUser {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 文档实体
     */
    private Doc doc;

    /**
     * 用户文档权限
     */
    private DocPermission permission;

    /**
     * 用户文档权限组
     */
    private DocPermissionGroup permissionGroup;

    /**
     * 文档个性化
     */
    private DocCustomization docCustomization;
}
