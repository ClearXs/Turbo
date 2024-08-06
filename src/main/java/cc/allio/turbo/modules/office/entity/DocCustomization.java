package cc.allio.turbo.modules.office.entity;

import cc.allio.turbo.common.db.entity.TenantEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("office_doc_customization")
@Schema(description = "文档个性化")
public class DocCustomization extends TenantEntity {

    /**
     * 文档id
     */
    @TableField("doc_id")
    @Schema(name = "文档id")
    private Long docId;

    /**
     * 用户id
     */
    @TableField("user_id")
    @Schema(name = "用户id")
    private Long userId;

    /**
     * 是否是分享的
     */
    @TableField("shared")
    @Schema(name = "是否是分享的")
    private Boolean shared;

    /**
     * 是否是喜爱的
     */
    @TableField("favorite")
    @Schema(name = "是否是喜爱的")
    private Boolean favorite;

    /**
     * 是否是喜爱的
     */
    @TableField("favor")
    @Schema(name = "是否是常用")
    private Boolean favor;
}