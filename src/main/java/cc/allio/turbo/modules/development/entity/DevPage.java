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
@TableName("dev_page")
@Schema(description = "页面")
public class DevPage extends TenantEntity implements CategoryEntity {

    /**
     * 页面名称
     */
    @TableField("name")
    @Schema(description = "页面名称")
    @NotBlank
    private String name;

    /**
     * 页面编码
     */
    @TableField("code")
    @Schema(description = "页面编码")
    @NotBlank
    @Unique
    private String code;

    /**
     * 页面路由
     */
    @TableField("route")
    @Schema(description = "页面路由")
    private String route;

    /**
     * 数据集id
     */
    @TableField("dataset_id")
    @Schema(description = "数据集id")
    private Long datasetId;

    /**
     * 数据视图
     */
    @TableField("dataview")
    @Schema(description = "数据视图")
    private String dataview;

    /**
     * bo
     */
    @TableField("bo_id")
    @Schema(description = "bo")
    private Long boId;

    /**
     * 表单
     */
    @TableField("form_id")
    @Schema(description = "表单")
    private Long formId;

    /**
     * 分类id
     */
    @TableField("category_id")
    @Schema(description = "分类id")
    private Long categoryId;
}
