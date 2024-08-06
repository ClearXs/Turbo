package cc.allio.turbo.modules.office.entity;

import cc.allio.turbo.common.db.entity.TenantEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("office_doc_changes")
@Schema(description = "文档变更情况")
public class DocChanges extends TenantEntity {

    /**
     * 用户id
     */
    @TableField("user_id")
    @Schema(description = "用户id")
    private Long userId;

    /**
     * 文档id
     */
    @TableField("doc_id")
    @Schema(description = "文档id")
    private Long docId;

    /**
     * 修改动作;如创建、分享
     */
    @TableField("action")
    @Schema(name = "修改动作", example = "如创建、分享")
    private String action;
}
