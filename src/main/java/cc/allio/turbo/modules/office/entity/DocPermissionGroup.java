package cc.allio.turbo.modules.office.entity;

import cc.allio.turbo.common.db.entity.TenantEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("office_doc_permission_group")
@Schema(description = "文档权限组")
public class DocPermissionGroup extends TenantEntity {

    /**
     * 文档id
     */
    @Schema(name = "文档id")
    @TableField("doc_id")
    private Long docId;

    /**
     * 组名
     */
    @Schema(name = "组名")
    @TableField("group_name")
    private String groupName;

    /**
     * 组编码
     */
    @Schema(name = "组编码")
    @TableField("group_code")
    private String groupCode;

    /**
     * 组权限
     */
    @Schema(name = "组权限")
    @TableField("permission")
    private String permission;
}
