package cc.allio.turbo.modules.development.entity;

import cc.allio.turbo.common.db.constraint.Unique;
import cc.allio.turbo.common.db.entity.CategoryEntity;
import cc.allio.turbo.common.db.entity.TenantEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("dev_form")
@Schema(description = "表单")
public class DevForm extends TenantEntity implements CategoryEntity {

    /**
     * 表单名称
     */
    @TableField("name")
    @Schema(description = "表单名称")
    @NotBlank
    private String name;

    /**
     * 表单编码
     */
    @TableField("code")
    @Schema(description = "表单编码")
    @NotBlank
    @Unique
    private String code;

    /**
     * 表单schema
     */
    @TableField("schema")
    @Schema(description = "表单schema数据")
    private String schema;

    /**
     * 所属于bo id
     */
    @TableField("bo_id")
    @Schema(description = "所属的bo")
    private Long boId;

    /**
     * 分类id
     */
    @TableField("category_id")
    @Schema(description = "分类id")
    private Long categoryId;

    /**
     * 备注
     */
    @TableField("remark")
    @Schema(description = "备注")
    private String remark;
}
