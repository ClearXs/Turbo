package cc.allio.turbo.modules.office.entity;

import cc.allio.turbo.common.db.entity.TenantEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("office_doc_cooperator")
@Schema(description = "文档协作者")
public class DocCooperator extends TenantEntity {

    /**
     * 协作者
     */
    @TableField("cooperator")
    @Schema(name = "协作者", example = "系统用户")
    private Long cooperator;

    /**
     * 文档ID
     */
    @TableField("doc_id")
    @Schema(name = "文档ID")
    private Long docId;

    /**
     * 权限组ID
     */
    @TableField("permission_group_id")
    @Schema(name = "权限组ID")
    private Long permissionGroupId;
}
