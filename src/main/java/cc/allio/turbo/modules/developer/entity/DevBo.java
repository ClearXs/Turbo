package cc.allio.turbo.modules.developer.entity;

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
@TableName("dev_bo")
@Schema(description = "业务对象")
public class DevBo extends TenantEntity implements CategoryEntity {

    /**
     * 业务对象名称
     */
    @TableField("name")
    @Schema(description = "业务对象名称")
    @NotBlank
    private String name;

    /**
     * 业务对象编码
     */
    @TableField("code")
    @Schema(description = "业务对象编码")
    @NotBlank
    private String code;

    /**
     * 备注
     */
    @TableField("remark")
    @Schema(description = "备注")
    private String remark;

    /**
     * 数据源id
     */
    @TableField("datasource_id")
    @Schema(description = "数据源id")
    private Long dataSourceId;

    /**
     * 分类id
     */
    @TableField("category_id")
    @Schema(description = "分类id")
    private Long categoryId;

    /**
     * 是否物化
     */
    @TableField("is_materialize")
    @Schema(description = "是否物化")
    private boolean materialize;
}
