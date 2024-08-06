package cc.allio.turbo.modules.office.entity;

import cc.allio.turbo.common.db.entity.TenantEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("office_doc_trash")
@Schema(description = "文档回收站")
public class DocTrash extends TenantEntity {

    /**
     * 文档ID
     */
    @Schema(name = "文档ID")
    @TableField("doc_id")
    private Long docId;

    /**
     * 用户id
     */
    @Schema(name = "用户id")
    @TableField("user_id")
    private Long userId;
}
